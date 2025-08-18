package com.thang.spotify.controller;

import com.thang.spotify.dto.request.auth.LoginRequest;
import com.thang.spotify.dto.request.auth.RefreshTokenRequest;
import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.dto.response.auth.LoginResponse;
import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.service.RedisService;
import com.thang.spotify.service.impl.security.JwtTokenService;
import com.thang.spotify.service.impl.UserServiceImpl;
import com.thang.spotify.service.impl.security.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(method= "POST", summary="Login account", description="This API allows you to check login response")
    @PostMapping("/login")
    public ResponseEntity<ResponseData<?>> login(@Valid @RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();

        String accessToken = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

        redisService.setValue(
                "refresh:" + userDetails.getUsername(),
                refreshToken,
                7, TimeUnit.DAYS
        );

        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);

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
    public ResponseEntity<ResponseData<Void>> logout(HttpServletRequest request) {
        // Nothing just return the message
        log.info("Request logout, username: {}", request.getUserPrincipal().getName());
        redisService.deleteKey("refresh:" + request.getUserPrincipal().getName());
        return ResponseEntity.status(200)
                .body(new ResponseData<>(200, "Logout successful", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseData<?>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseData<>(401, "Invalid refresh token", null));
        }

        String username = jwtTokenService.getEmailFromJwtToken(refreshToken);
        String storedToken = redisService.getValue("refresh:" + username);

        if (storedToken != null && storedToken.equals(refreshToken)) {
            SecurityUserDetails userDetails = (SecurityUserDetails) userDetailsServiceImpl.loadUserByUsername(username);
            String newAccessToken = jwtTokenService.generateAccessToken(userDetails);
            ResponseData<LoginResponse> responseData = new ResponseData<>(200, "Token refreshed successfully", new LoginResponse(newAccessToken, refreshToken));

            return ResponseEntity.ok(responseData);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseData<>(401, "Invalid refresh token", null));
    }

}