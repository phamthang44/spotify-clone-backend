package com.thang.spotify.service.oauth2;

import com.thang.spotify.common.enums.AuthProvider;
import com.thang.spotify.common.enums.ErrorCode;
import com.thang.spotify.common.enums.UserStatus;
import com.thang.spotify.common.enums.UserType;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.entity.Role;
import com.thang.spotify.entity.User;
import com.thang.spotify.entity.UserAsset;
import com.thang.spotify.exception.ResourceNotFoundException;
import com.thang.spotify.exception.TokenNotFoundException;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.repository.RoleRepository;
import com.thang.spotify.repository.UserRepository;
import com.thang.spotify.service.UserAssetService;
import com.thang.spotify.service.UserService;
import com.thang.spotify.service.impl.security.JwtTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleOAuth2ServiceImpl implements GoogleOAuth2Service {

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final WebClient webClient;
    private final UserAssetService userAssetService;

    private final Set<String> usedCodes = ConcurrentHashMap.newKeySet();

    @Override
    public String buildAuthorizationUrl() {
        var google = oAuth2ClientProperties.getRegistration().get("google");

        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", google.getClientId())
                .queryParam("redirect_uri", google.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .queryParam("access_type", "offline")
                .build().toUriString();
    }

    @Override
    @Transactional
    public GoogleUserInfo processGoogleOAuth2Code(String code) {
        if (usedCodes.contains(code)) {
            log.warn("Code already used: {}", code);
            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Authorization code has already been used");
        }

        usedCodes.add(code);
        log.info("Processing Google OAuth2 code: {}", code);

        try {
            String accessToken = getAccessTokenFromCode(code);
            Map<String, Object> attributes = getUserInfoFromAccessToken(accessToken);
            log.info("User attributes from Google: {}", attributes);
            OAuth2UserInfo oAuth2User = OAuth2UserInfoFactory.getOAuth2UserInfo("google", attributes);
            String email = oAuth2User.getEmail();
            String name = oAuth2User.getName();
            String googleId = oAuth2User.getId();
            String imageUrl = oAuth2User.getImageUrl();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                Role role = roleRepository.findByName(UserType.LISTENER.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

                User newUser = new User();
                newUser.setEmail(email);
                newUser.setDisplayName(name != null ? name : "User");
                newUser.setRole(role);
                newUser.setStatus(UserStatus.INCOMPLETE);
                newUser.setAuthProvider(AuthProvider.GOOGLE);
                newUser.setProviderId(googleId);
                Long userId = userRepository.save(newUser).getId();
                if (!Util.isNullOrBlank(imageUrl)) {
                    userAssetService.addNewAvatar(userId, imageUrl);
                }
                log.info("OAuth2 user registered successfully with ID: {}", userId);
            } else {
                log.info("OAuth2 user logged in successfully with ID: {}", user.getId());
            }
            return new GoogleUserInfo(attributes);
        } catch (Exception e) {
            log.error("Error processing Google OAuth2 code: {}", e.getMessage());
            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Error processing Google OAuth2 code: " + e.getMessage());
        }
    }

    @Override
    public GoogleUserInfo getUserInfoFromCode(String code) {
        String accessToken = getAccessTokenFromCode(code);
        Map<String, Object> attributes = getUserInfoFromAccessToken(accessToken);
        return new GoogleUserInfo(attributes);
    }

    @Override
    public String getAccessTokenFromCode(String code) {
        var google = oAuth2ClientProperties.getRegistration().get("google");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", google.getClientId());
        formData.add("client_secret", google.getClientSecret());
        formData.add("redirect_uri", google.getRedirectUri());
        formData.add("grant_type", "authorization_code");

        try {
            Map<String, Object> tokenResponse = webClient.post()
                    .uri("https://oauth2.googleapis.com/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("Google OAuth2 Error Response: {}", errorBody);
                                return Mono.error(new TokenNotFoundException(ErrorCode.INVALID_TOKEN,
                                        "Google OAuth2 failed: " + errorBody));
                            }))
                    .bodyToMono(Map.class)
                    .block();

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                String error = tokenResponse != null ? (String) tokenResponse.get("error") : "null response";
                String errorDescription = tokenResponse != null ? (String) tokenResponse.get("error_description") : "No response received";
                log.error("Error from Google token exchange: {} - {}", error, errorDescription);
                throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Failed to exchange code for access token: " + errorDescription);
            }

            return (String) tokenResponse.get("access_token");

        } catch (Exception e) {
            log.error("Error getting access token: {}", e.getMessage());
            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Error getting access token: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getUserInfoFromAccessToken(String accessToken) {
        try {
            return webClient.get()
                    .uri("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            log.error("Error getting user info: {}", e.getMessage());
            throw new RuntimeException("Error getting user info from Google: " + e.getMessage());
        }
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        var google = oAuth2ClientProperties.getRegistration().get("google");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("refresh_token", refreshToken);
        formData.add("client_id", google.getClientId());
        formData.add("client_secret", google.getClientSecret());
        formData.add("grant_type", "refresh_token");

        try {
            // Gửi yêu cầu làm mới Access Token
            Map<String, Object> tokenResponse = webClient.post()
                    .uri("https://oauth2.googleapis.com/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("Google OAuth2 Error Response: {}", errorBody);
                                return Mono.error(new TokenNotFoundException(ErrorCode.INVALID_TOKEN,
                                        "Google OAuth2 failed: " + errorBody));
                            }))
                    .bodyToMono(Map.class)
                    .block();

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                String error = tokenResponse != null ? (String) tokenResponse.get("error") : "null response";
                String errorDescription = tokenResponse != null ? (String) tokenResponse.get("error_description") : "No response received";
                log.error("Error from Google token refresh: {} - {}", error, errorDescription);
                throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Failed to refresh access token: " + errorDescription);
            }

            return (String) tokenResponse.get("access_token");

        } catch (Exception e) {
            log.error("Error refreshing access token: {}", e.getMessage());
            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Error refreshing access token: " + e.getMessage());
        }
    }


}



//    @Override
//    @Transactional
//    public void processGoogleOAuth2Code(String code) {
//        if (usedCodes.contains(code)) {
//            log.warn("Code already used: {}", code);
//            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN,
//                    "Authorization code has already been used");
//        }
//        usedCodes.add(code);
//        log.info("Processing Google OAuth2 code: {}", code);
//        var google = oAuth2ClientProperties.getRegistration().get("google");
//        // Log the exact values being sent
//        log.info("=== OAuth2 Debug Info ===");
//        log.info("Client ID: {}", google.getClientId());
//        log.info("Redirect URI from config: '{}'", google.getRedirectUri());
//        log.info("Authorization code: {}", code);
//        log.info("Code length: {}", code.length());
//
//        try {
//            // Create form data
//            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//            formData.add("code", code);
//            formData.add("client_id", google.getClientId());
//            formData.add("client_secret", google.getClientSecret());
//            formData.add("redirect_uri", google.getRedirectUri());
//            formData.add("grant_type", "authorization_code");
//
//            log.info("Sending token request to Google with redirect_uri: '{}'", google.getRedirectUri());
//
//            Map tokenResponse = webClient.post()
//                    .uri("https://oauth2.googleapis.com/token")
//                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                    .bodyValue(formData)
//                    .retrieve()
//                    .onStatus(HttpStatusCode::isError, response -> {
//                        return response.bodyToMono(String.class)
//                                .flatMap(errorBody -> {
//                                    log.error("Google OAuth2 Error Response: {}", errorBody);
//                                    return Mono.error(new TokenNotFoundException(
//                                            ErrorCode.INVALID_TOKEN,
//                                            "Google OAuth2 failed: " + errorBody));
//                                });
//                    })
//                    .bodyToMono(Map.class)
//                    .block();
//
//            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
//                String error = tokenResponse != null ? (String) tokenResponse.get("error") : "null response";
//                String errorDescription = tokenResponse != null ? (String) tokenResponse.get("error_description") : "No response received";
//                log.error("Error from Google token exchange: {} - {}", error, errorDescription);
//                throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Failed to exchange code for access token: " + errorDescription);
//            }
//
//            log.info("Token response: {}", tokenResponse);
//            // Lấy attributes từ Google
//            Map attributes = webClient.get()
//                    .uri("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + tokenResponse.get("access_token"))
//                    .retrieve()
//                    .bodyToMono(Map.class)
//                    .block();
//
//            //Dùng factory để tạo OAuth2UserInfo
//            OAuth2UserInfo oAuth2User = OAuth2UserInfoFactory.getOAuth2UserInfo("google", attributes);
//
//            //Lấy thông tin cần thiết
//            String email = oAuth2User.getEmail();
//            String name = oAuth2User.getName();
//            String googleId = oAuth2User.getId();
//            String imageUrl = oAuth2User.getImageUrl();
//
//            User user = userRepository.findByEmail(email).orElse(null);
//
//            if (user != null) {
//                SecurityUserDetails userDetails = SecurityUserDetails.build(user);
//                log.info("OAuth2 user logged in successfully with ID: {}", user.getId());
//            } else {
//                Role role = roleRepository.findByName(UserType.LISTENER.getName())
//                        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
//                User newUser = new User();
//                newUser.setEmail(email);
//                newUser.setDisplayName(name != null ? name : "User");
//                newUser.setRole(role);
//                newUser.setStatus(UserStatus.INCOMPLETE);
//                newUser.setAuthProvider(AuthProvider.GOOGLE); // or AuthProvider.FACEBOOK based on the provider
//                newUser.setProviderId(googleId); // You can set the provider ID if available
//                Long userId = userRepository.save(newUser).getId();
//                log.info("OAuth2 user registered successfully with ID: {}", userId);
//            }
//
//        } catch (HttpClientErrorException e) {
//            log.error("=== Google OAuth2 Error Details ===");
//            log.error("Status Code: {}", e.getStatusCode());
//            log.error("Response Body: {}", e.getResponseBodyAsString());
//            log.error("Request URL: https://oauth2.googleapis.com/token");
//            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN,
//                    "Google OAuth2 Error: " + e.getResponseBodyAsString());
//        }
//    }
