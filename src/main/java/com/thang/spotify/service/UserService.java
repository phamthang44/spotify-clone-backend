package com.thang.spotify.service;

import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.entity.User;

public interface UserService {

    long registerUser(RegisterRequest registerRequestDTO);
    void  verifyEmail(String token);
}
