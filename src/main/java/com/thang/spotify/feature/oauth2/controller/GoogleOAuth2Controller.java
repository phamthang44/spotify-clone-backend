package com.thang.spotify.feature.oauth2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thang.spotify.common.enums.UserStatus;
import com.thang.spotify.common.dto.ResponseData;
import com.thang.spotify.feature.auth.dto.response.LoginResponse;
import com.thang.spotify.feature.auth.dto.response.OAuth2Response;
import com.thang.spotify.feature.user.entity.User;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.feature.auth.service.RefreshTokenService;
import com.thang.spotify.feature.user.service.UserService;
import com.thang.spotify.feature.auth.service.impl.JwtTokenService;
import com.thang.spotify.feature.oauth2.service.GoogleOAuth2Service;
import com.thang.spotify.feature.oauth2.model.GoogleUserInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/oauth2")
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2Controller {

    private final GoogleOAuth2Service googleOAuth2Service;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String url = googleOAuth2Service.buildAuthorizationUrl();
        response.sendRedirect(url);
    }

    @GetMapping("/google/callback")
    public void handleGoogleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        GoogleUserInfo oAuth2UserInfo = googleOAuth2Service.processGoogleOAuth2Code(code);
        User existingUser = userService.findByEmail(oAuth2UserInfo.getEmail());

        if (existingUser == null) {
            // User does not exist, proceed with signup flow
            OAuth2Response oAuth2Response = OAuth2Response.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .name(oAuth2UserInfo.getName())
                    .isExistingUser(false)
                    .status(UserStatus.INCOMPLETE.toString())
                    .requiredFields(List.of("displayName", "gender", "dateOfBirth"))
                    .build();
            ResponseData<OAuth2Response> responseData = new ResponseData<>(HttpStatus.OK.value(), "User not found, additional info required", oAuth2Response);
            String json = new ObjectMapper().writeValueAsString(responseData);
            log.info("OAuth2 signup user");
            String script = "<script>" +
                    "window.opener.postMessage(" + json + ", 'http://localhost:3000/signup');" +
                    "window.close();" +
                    "</script>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(script);
        } else {
            if (existingUser.getStatus().equals(UserStatus.INCOMPLETE)) {
                // User exists but has incomplete profile
                OAuth2Response oAuth2Response = OAuth2Response.builder()
                        .email(oAuth2UserInfo.getEmail())
                        .name(oAuth2UserInfo.getName())
                        .isExistingUser(true)
                        .status(UserStatus.INCOMPLETE.toString())
                        .requiredFields(List.of("displayName", "gender", "dateOfBirth"))
                        .build();
                ResponseData<OAuth2Response> responseData = new ResponseData<>(HttpStatus.OK.value(), "User found, but additional info required", oAuth2Response);
                String json = new ObjectMapper().writeValueAsString(responseData);
                log.info("OAuth2 incomplete user");
                String script = "<script>" +
                        "window.opener.postMessage(" + json + ", 'http://localhost:3000');" +
                        "window.close();" +
                        "</script>";
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write(script);
            }
        }

        if (existingUser != null && existingUser.getStatus().equals(UserStatus.ACTIVE)) {
            // User exists and has complete profile, proceed with login
            SecurityUserDetails userDetails = SecurityUserDetails.build(existingUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenService.generateAccessToken(userDetails);
            LoginResponse loginResponse = new LoginResponse(jwt, existingUser.getStatus());
            ResponseData<LoginResponse> responseData = new ResponseData<>(HttpStatus.OK.value(), "Login successful", loginResponse);
            String newRefreshToken = refreshTokenService.createOrUpdateRefreshToken(existingUser);

            ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .sameSite("Strict")
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            String json = new ObjectMapper().writeValueAsString(responseData);

            log.info("OAuth2 login successful for user: {}", existingUser.getEmail());

            String script = "<script>" +
                    "window.opener.postMessage(" + json + ", 'http://localhost:3000');" +
                    "window.close();" +
                    "</script>";
            response.setContentType("text/html");
            response.getWriter().write(script);
        }
    }
}

//        String script;
//
//        if (existingUser != null) {
//            if (existingUser.getStatus().equals(UserStatus.INCOMPLETE)) {
//                // Người dùng đã tồn tại nhưng chưa hoàn tất thông tin
//                OAuth2Response oAuth2Response = OAuth2Response.builder()
//                        .email(email)
//                        .name((String) userInfo.get("name"))
//                        .isExistingUser(true)
//                        .status(UserStatus.INCOMPLETE.toString())
//                        .requiredFields(List.of("displayName", "gender", "dateOfBirth"))
//                        .build();
//
//                ResponseData<OAuth2Response> responseData = new ResponseData<>(HttpStatus.OK.value(), "User found, but additional info required", oAuth2Response);
//                String json = new ObjectMapper().writeValueAsString(responseData);
//                log.info("OAuth2 incomplete user");
//
//                script = "<script>" +
//                        "window.opener.postMessage(" + json + ", 'http://localhost:3000/signup');" +
//                        "window.close();" +
//                        "</script>";
//            } else {
//                // Nếu người dùng đã hoàn tất thông tin, thực hiện đăng nhập
//                SecurityUserDetails userDetails = SecurityUserDetails.build(existingUser);
//                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                String jwt = jwtTokenService.generateAccessToken(userDetails);
//                LoginResponse loginResponse = new LoginResponse(jwt, existingUser.getStatus());
//                ResponseData<LoginResponse> responseData = new ResponseData<>(HttpStatus.OK.value(), "Login successful", loginResponse);
//                String json = new ObjectMapper().writeValueAsString(responseData);
//
//                log.info("OAuth2 login successful for user: {}", existingUser.getEmail());
//
//                script = "<script>" +
//                        "window.opener.postMessage(" + json + ", 'http://localhost:3000');" +
//                        "window.close();" +
//                        "</script>";
//            }
//        } else {
//            //tạo mới user here
//            googleOAuth2Service.processGoogleOAuth2Code(code); // Đảm bảo chỉ gọi một lần
//
//            OAuth2Response oAuth2Response = OAuth2Response.builder()
//                    .email(email)
//                    .name((String) userInfo.get("name"))
//                    .isExistingUser(false)
//                    .status("INCOMPLETE")
//                    .requiredFields(List.of("displayName", "gender", "dateOfBirth"))
//                    .build();
//
//            ResponseData<OAuth2Response> responseData = new ResponseData<>(HttpStatus.OK.value(), "User not found, additional info required", oAuth2Response);
//            String json = new ObjectMapper().writeValueAsString(responseData);
//            log.info("OAuth2 signup user");
//
//            script = "<script>" +
//                    "window.opener.postMessage(" + json + ", 'http://localhost:3000/signup');" +
//                    "window.close();" +
//                    "</script>";
//        }
//        response.setContentType("text/html");
//        response.getWriter().write(script);