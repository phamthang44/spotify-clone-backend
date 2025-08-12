package com.thang.spotify.controller;

import com.thang.spotify.dto.request.auth.LoginRequest;
import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.dto.response.auth.LoginResponse;
import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.service.impl.security.JwtTokenService;
import com.thang.spotify.service.impl.user.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserServiceImpl userService;


    @Operation(method= "POST", summary="Login account", description="This API allows you to check login response")
    @PostMapping("/login")
    public ResponseEntity<ResponseData<?>> login(@Valid @RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();

        String token = jwtTokenService.generateToken(userDetails);

        LoginResponse loginResponse = new LoginResponse(200, "Login successful", token);

        return ResponseEntity.status(200).body(loginResponse);
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
        return ResponseEntity.status(200)
                .body(new ResponseData<>(200, "Logout successful", null));
    }


}








//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//
//        UserDetails userDetails = (UserDetails) auth.getPrincipal();
//
//
//        UserDetailsResponse userResponse = UserDetailsResponse.builder()
//                .email(userDetails.getUsername())
//                .build();
//
//        String token = jwtTokenService.generateToken(userDetails);
//        LoginResponse loginResponse = new LoginResponse(200, "Login successful", userResponse, token);
//        return ResponseEntity.ok(loginResponse);
//        try {
//
//
//        } catch (BadCredentialsException ex) {
//            log.error("Login failed for email: {}", request.getEmail());
//            return ResponseEntity.status(401).body(new ResponseError(401, "Invalid email or password"));
//        } catch (UsernameNotFoundException ex) {
//            log.error("User not found: {}", request.getEmail());
//            return ResponseEntity.status(401).body(new ResponseError(401, "Invalid email or password"));
//        } catch (Exception ex) {
//            log.error("Unexpected error during login: {}", ex.getMessage());
//            return ResponseEntity.status(500).body(new ResponseError(500, "Internal server error"));
//        }