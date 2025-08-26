package com.thang.spotify.controller;

import com.thang.spotify.common.enums.UserStatus;
import com.thang.spotify.dto.request.auth.LoginRequest;
import com.thang.spotify.dto.request.auth.RefreshTokenRequest;
import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.dto.response.auth.LoginResponse;
import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.entity.RefreshToken;
import com.thang.spotify.entity.User;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.service.RedisService;
import com.thang.spotify.service.RefreshTokenService;
import com.thang.spotify.service.impl.security.JwtTokenService;
import com.thang.spotify.service.impl.UserServiceImpl;
import com.thang.spotify.service.impl.security.UserDetailsServiceImpl;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/users")
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

    @Operation(method= "POST", summary="Login account", description="This API allows you to check login response")
    @PostMapping("/login")
    public ResponseEntity<ResponseData<?>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();

        String accessToken = jwtTokenService.generateAccessToken(userDetails);

        User user = userDetails.getUser();
        UserStatus status = user.getStatus();
        String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user);

//        redisService.setValue(
//                "refresh:" + userDetails.getUsername(),
//                refreshToken.getToken(),
//                7, TimeUnit.DAYS
//        );
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // Chỉ gửi cookie qua HTTPS
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict") // SameSite policy
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        LoginResponse loginResponse = new LoginResponse(accessToken, status);

        return ResponseEntity.status(200).body(new ResponseData<>(200, "Login successfully", loginResponse));
    }

    @Operation(method= "POST", summary="Add user", description="This API allows you to add a new user")
    @PostMapping(value = "/register")
    public ResponseData<?> register(@Valid @RequestBody RegisterRequest user) {

        log.info("Request add user = {}", user.getEmail());
        long userId = userService.registerUser(user);

        return new ResponseData<>(201, "User registered successfully", userId);

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
        String rawRefreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (rawRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseData<>(401, "No refresh token provided", null));
        }

        RefreshToken tokenEntity = refreshTokenService.validateRefreshToken(rawRefreshToken); // service tìm hash và verify
        if (tokenEntity == null || tokenEntity.isRevoked() || tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseData<>(401, "Invalid or expired refresh token", null));
        }
        User user = tokenEntity.getUser();
        UserStatus status = user.getStatus();
        SecurityUserDetails userDetails = (SecurityUserDetails) userDetailsServiceImpl.loadUserByUsername(user.getEmail());

        String newAccessToken = jwtTokenService.generateAccessToken(userDetails);

        String newRefreshToken = refreshTokenService.createOrUpdateRefreshToken(user);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(
                new ResponseData<>(200, "Token refreshed successfully", new LoginResponse(newAccessToken, status))
        );
    }

    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);   // Ngăn JS truy cập
        cookie.setSecure(true);     // Chỉ gửi qua HTTPS (bắt buộc ở production)
        cookie.setPath("/"); // Chỉ gửi cookie tới endpoint này
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày (tuỳ vào logic)
        response.addCookie(cookie);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully!");
    }

}