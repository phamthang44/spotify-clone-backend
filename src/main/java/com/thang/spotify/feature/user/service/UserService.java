package com.thang.spotify.feature.user.service;

import com.thang.spotify.feature.auth.dto.request.RegisterRequest;
import com.thang.spotify.feature.user.dto.response.UserResponse;
import com.thang.spotify.feature.user.entity.User;

public interface UserService {

    long registerUser(RegisterRequest registerRequestDTO);
    void  verifyEmail(String token);
    User findByEmail(String email);
    long completeOAuth2Signup(User user, RegisterRequest registerRequestDTO);
    void resendVerificationEmail(String email);
    User getUserById(Long id);
    UserResponse getUserResponse(Long id);
}
