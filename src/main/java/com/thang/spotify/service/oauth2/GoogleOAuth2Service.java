package com.thang.spotify.service.oauth2;

public interface GoogleOAuth2Service {
    String buildAuthorizationUrl();
    void processGoogleOAuth2Code(String code);
    GoogleUserInfo getUserInfoFromCode(String code);
}
