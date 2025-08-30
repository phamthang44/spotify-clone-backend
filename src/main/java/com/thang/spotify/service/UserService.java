package com.thang.spotify.service;

import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.dto.response.auth.OAuth2Response;
import com.thang.spotify.entity.User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

public interface UserService {

    long registerUser(RegisterRequest registerRequestDTO);
    void  verifyEmail(String token);
    User findByEmail(String email);
    long completeOAuth2Signup(User user, RegisterRequest registerRequestDTO);
    void resendVerificationEmail(String email);
    User getUserById(Long id);

}
