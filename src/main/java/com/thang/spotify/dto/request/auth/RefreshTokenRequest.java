package com.thang.spotify.dto.request.auth;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}
