package com.thang.spotify.feature.auth.controller;

import com.thang.spotify.common.enums.UserStatus;
import com.thang.spotify.feature.auth.dto.request.EmailRequest;
import com.thang.spotify.feature.auth.dto.request.LoginRequest;
import com.thang.spotify.feature.auth.dto.request.RegisterRequest;
import com.thang.spotify.feature.auth.dto.response.LoginResponse;
import com.thang.spotify.common.dto.ResponseData;
import com.thang.spotify.feature.auth.entity.RefreshToken;
import com.thang.spotify.feature.user.entity.User;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.infra.redis.service.RedisService;
import com.thang.spotify.feature.auth.service.RefreshTokenService;
import com.thang.spotify.feature.auth.service.impl.JwtTokenService;
import com.thang.spotify.feature.user.service.impl.UserServiceImpl;
import com.thang.spotify.feature.auth.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserServiceImpl userService;
    private final RedisService redisService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Operation(method= "POST", summary="Login account", description="This API allows you to check login response")
    @PostMapping("/login")
    public ResponseEntity<ResponseData<?>> login(@Valid @RequestBody LoginRequest requestLogin, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword())
        );

        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        //3. Luôn tạo access token mới
        String accessToken = jwtTokenService.generateAccessToken(userDetails);

        String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // Chỉ gửi cookie qua HTTPS
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict") // SameSite policy
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        LoginResponse loginResponse = new LoginResponse(accessToken, user.getStatus());

        return ResponseEntity.status(200).body(new ResponseData<>(200, "Login successfully", loginResponse));
    }

    @Operation(method= "POST", summary="Add user", description="This API allows you to add a new user")
    @PostMapping(value = "/register")
    public ResponseEntity<ResponseData<?>> register(@Valid @RequestBody RegisterRequest user) {

        log.info("Request add user = {}", user.getEmail());
        long userId = userService.registerUser(user);

        return ResponseEntity.status(201)
                .body(new ResponseData<>(201, "User registered successfully", userId));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseData<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        // Nothing just return the message
        String rawRefreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (rawRefreshToken != null) {
            RefreshToken tokenEntity = refreshTokenService.getRefreshToken(rawRefreshToken);
            if (tokenEntity != null) {
                refreshTokenService.revokeRefreshToken(rawRefreshToken);
            }
        }

        ResponseCookie clearCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)          // match môi trường dev
                .path("/")              // match cookie gốc
                .maxAge(0)
                .sameSite("Strict")
                .build();
        log.info("Set-Cookie header: {}", clearCookie);
        response.setHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());

        return ResponseEntity.status(200)
                .body(new ResponseData<>(200, "Logout successful", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseData<?>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // --- 1. Lấy refresh token từ cookie ---
        String rawRefreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (rawRefreshToken == null) {
            return unauthorized("No refresh token provided");
        }

        // --- 2. Validate refresh token ---
        RefreshToken tokenEntity = refreshTokenService.validateRefreshToken(rawRefreshToken);
        if (tokenEntity == null || tokenEntity.isRevoked()) {
            return unauthorized("Invalid or revoked refresh token");
        }

        User user = tokenEntity.getUser();
        UserStatus status = user.getStatus();

        // --- 3. Check user status ---
        if (status.equals(UserStatus.UNVERIFIED)) {
            return forbidden("Email not verified. Please verify your email: " + user.getEmail());
        }
        if (!status.equals(UserStatus.ACTIVE) && !status.equals(UserStatus.INCOMPLETE)) {
            return forbidden("User account is not active");
        }

        //set context authentication
        SecurityUserDetails userDetails =
                (SecurityUserDetails) userDetailsServiceImpl.loadUserByUsername(user.getEmail());

        // --- 4. Generate new access token ---
        String newAccessToken = jwtTokenService.generateAccessToken(userDetails);

        // --- 5. Nếu refresh token còn hạn thì dùng lại ---
        if (tokenEntity.getExpiryDate().isAfter(LocalDateTime.now())) {
            log.info("Refresh token is still valid for user: {}", user.getEmail());
            return ok("Token refreshed successfully", new LoginResponse(newAccessToken, status));
        }

        // --- 6. Nếu hết hạn thì rotate refresh token ---
        String newRefreshToken = refreshTokenService.createOrUpdateRefreshToken(user);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ok("Token refreshed successfully", new LoginResponse(newAccessToken, status));
    }

    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);   // Ngăn JS truy cập
        cookie.setSecure(true);     // Chỉ gửi qua HTTPS (bắt buộc ở production)
        cookie.setPath("/"); // Chỉ gửi cookie tới endpoint này
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày (tuỳ vào logic)
        response.addCookie(cookie);
    }

    @GetMapping("/email-from-cookie")
    public ResponseEntity<ResponseData<?>> getEmailFromCookie(HttpServletRequest request) {
        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData<>(404, "Refresh token not found", null));
        }

        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseData<>(401, "Invalid or expired refresh token", null));
        }
        String email = token.getUser().getEmail();
        return ResponseEntity.ok(new ResponseData<>(200, "Email retrieved successfully", email));
    }

    @GetMapping("/users/status")
    public ResponseEntity<ResponseData<?>> getUserStatus(HttpServletRequest request) {
        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData<>(404, "Refresh token not found", null));
        }

        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseData<>(401, "Invalid or expired refresh token", null));
        }

        UserStatus status = token.getUser().getStatus();
        if (status.equals(UserStatus.UNVERIFIED)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseData<>(403, "Email not verified. Please verify your email: ", token.getUser().getEmail()));
        } else if (status.equals(UserStatus.ACTIVE)) {
            return ResponseEntity.ok(new ResponseData<>(200, "User status is ACTIVE", status) );
        }
        else {
            return ResponseEntity.ok(new ResponseData<>(200, "User status retrieved successfully", status) );
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully!");
    }
    @PostMapping("/resend-verification")
    public ResponseEntity<ResponseData<?>> resendVerificationEmail(@RequestBody EmailRequest dto) {
        log.info("Resend verification email to {}", dto.getEmail());
        userService.resendVerificationEmail(dto.getEmail());
        return ResponseEntity.ok(new ResponseData<>(200, "Verification email resent successfully", null));
    }

    @PostMapping("/complete-signup")
    public ResponseEntity<ResponseData<?>> completeSignup(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = userService.findByEmail(registerRequest.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + registerRequest.getEmail());
        } if (!user.getStatus().equals(UserStatus.INCOMPLETE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseData<>(400, "User status is not INCOMPLETE", null));
        }
        long userId = userService.completeOAuth2Signup(user, registerRequest);
        return ResponseEntity.ok(new ResponseData<>(200, "Signup completed successfully", userId));
    }

    private ResponseEntity<ResponseData<?>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseData<>(401, message, null));
    }

    private ResponseEntity<ResponseData<?>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseData<>(403, message, null));
    }

    private ResponseEntity<ResponseData<?>> ok(String message, Object data) {
        return ResponseEntity.ok(new ResponseData<>(200, message, data));
    }

}