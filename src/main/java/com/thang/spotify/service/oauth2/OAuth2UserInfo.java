package com.thang.spotify.service.oauth2;

public interface OAuth2UserInfo {
    String getId();
    String getEmail();
    String getName();
    String getImageUrl();
}