package com.thang.spotify.service.oauth2;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
                                                  java.util.Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleUserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase("facebook")) {
            return new FacebookUserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase("github")) {
            return new GithubUserInfo(attributes);
        } else {
            throw new IllegalArgumentException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

}
