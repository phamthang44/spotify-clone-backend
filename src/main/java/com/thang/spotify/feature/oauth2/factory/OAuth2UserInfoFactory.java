package com.thang.spotify.feature.oauth2.factory;

import com.thang.spotify.feature.oauth2.model.FacebookUserInfo;
import com.thang.spotify.feature.oauth2.model.GithubUserInfo;
import com.thang.spotify.feature.oauth2.model.GoogleUserInfo;
import com.thang.spotify.feature.oauth2.model.OAuth2UserInfo;

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
