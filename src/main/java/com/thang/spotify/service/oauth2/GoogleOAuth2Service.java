package com.thang.spotify.service.oauth2;

import java.util.Map;

public interface GoogleOAuth2Service {
    String buildAuthorizationUrl();
    GoogleUserInfo processGoogleOAuth2Code(String code);
    GoogleUserInfo getUserInfoFromCode(String code);
    String getAccessTokenFromCode(String code);
    Map<String, Object> getUserInfoFromAccessToken(String accessToken);
    String refreshAccessToken(String refreshToken);
}
