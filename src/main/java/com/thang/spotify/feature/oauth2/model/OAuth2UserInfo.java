package com.thang.spotify.feature.oauth2.model;

public interface OAuth2UserInfo {
    String getId();
    String getEmail();
    String getName();
    String getImageUrl();
}