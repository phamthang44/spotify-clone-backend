package com.thang.spotify.feature.auth.dto.request;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}
