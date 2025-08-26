package com.thang.spotify.service;

import com.thang.spotify.entity.RefreshToken;
import com.thang.spotify.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    String createOrUpdateRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    void revokeRefreshToken(String token);
    RefreshToken getRefreshToken(String refreshToken);
}
