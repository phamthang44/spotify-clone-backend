package com.thang.spotify.service.oauth2;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public FacebookUserInfo(Map<String, Object> attributes) {
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
