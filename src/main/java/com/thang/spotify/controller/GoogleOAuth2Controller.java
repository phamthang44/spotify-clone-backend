package com.thang.spotify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thang.spotify.common.enums.UserStatus;
import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.auth.LoginResponse;
import com.thang.spotify.dto.response.auth.OAuth2Response;
import com.thang.spotify.entity.User;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.service.UserService;
import com.thang.spotify.service.impl.security.JwtTokenService;
import com.thang.spotify.service.oauth2.GoogleOAuth2Service;
import com.thang.spotify.service.oauth2.GoogleUserInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/oauth2")
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2Controller {

    private final GoogleOAuth2Service googleOAuth2Service;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String url = googleOAuth2Service.buildAuthorizationUrl();
        response.sendRedirect(url);
    }

    @GetMapping("/google/callback")
    public void handleGoogleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        GoogleUserInfo userInfo = googleOAuth2Service.getUserInfoFromCode(code); //chỗ này gây lỗi vì lí do là code đã dùng rồi
        // cần fix here

        User existingUser = userService.findByEmail(userInfo.getEmail());
        String script;
        if (existingUser != null) {
            if (existingUser.getStatus().equals(UserStatus.INCOMPLETE)) {
                OAuth2Response oAuth2Response = OAuth2Response.builder()
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .isExistingUser(false)
                        .status(UserStatus.INCOMPLETE.toString())
                        .requiredFields(List.of("displayName", "gender", "dateOfBirth"))
                        .build();
                ResponseData<OAuth2Response> responseData = new ResponseData<>(
                        HttpStatus.OK.value(), "User found, but additional info required", oAuth2Response
                );
                String json = new ObjectMapper().writeValueAsString(responseData);
                log.info("OAuth2 incomplete user");
                script = "<script>" +
                        "window.opener.postMessage(" + json + ", 'http://localhost:3000/signup');" +
                        "window.close();" +
                        "</script>";

                response.setContentType("text/html");
                response.getWriter().write(script);
            }
            SecurityUserDetails userDetails = SecurityUserDetails.build(existingUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenService.generateAccessToken(userDetails);

            LoginResponse loginResponse = new LoginResponse(jwt, existingUser.getStatus());
            ResponseData<LoginResponse> responseData = new ResponseData<>(
                    HttpStatus.OK.value(), "Login successful", loginResponse
            );

            String json = new ObjectMapper().writeValueAsString(responseData);
            log.info("OAuth2 login successful for user: {}", existingUser.getEmail());
            script = "<script>" +
                    "window.opener.postMessage(" + json + ", 'http://localhost:3000');" +
                    "window.close();" +
                    "</script>";

            response.setContentType("text/html");
            response.getWriter().write(script);
        } else {
            googleOAuth2Service.processGoogleOAuth2Code(code);

            OAuth2Response oAuth2Response = OAuth2Response.builder()
                    .email(userInfo.getEmail())
                    .name(userInfo.getName())
                    .isExistingUser(false)
                    .status("INCOMPLETE")
                    .requiredFields(List.of("displayName", "gender", "dateOfBirth"))
                    .build();
            ResponseData<OAuth2Response> responseData = new ResponseData<>(
                    HttpStatus.OK.value(), "User not found, additional info required", oAuth2Response
            );
            String json = new ObjectMapper().writeValueAsString(responseData);
            log.info("OAuth2 signup user");
            script = "<script>" +
                    "window.opener.postMessage(" + json + ", 'http://localhost:3000/signup');" +
                    "window.close();" +
                    "</script>";

            response.setContentType("text/html");
            response.getWriter().write(script);
        }
    }
}