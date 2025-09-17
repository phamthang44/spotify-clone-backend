package com.thang.spotify.feature.auth.service;

import com.thang.spotify.feature.auth.entity.RefreshToken;
import com.thang.spotify.feature.user.entity.User;

public interface RefreshTokenService {

    String createOrUpdateRefreshToken(User user);
    RefreshToken validateRefreshToken(String token);
    void revokeRefreshToken(String token);
    RefreshToken getRefreshToken(String refreshToken);
}
