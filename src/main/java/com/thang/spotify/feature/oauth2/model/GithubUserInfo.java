package com.thang.spotify.feature.oauth2.model;

import java.util.Map;

public class GithubUserInfo implements OAuth2UserInfo{

    private final Map<String, Object> attributes;

    public GithubUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getImageUrl() {
        return "";
    }
}
