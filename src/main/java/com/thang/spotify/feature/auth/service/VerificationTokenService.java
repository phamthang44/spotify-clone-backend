package com.thang.spotify.feature.auth.service;

import com.thang.spotify.feature.user.entity.User;
import com.thang.spotify.feature.auth.entity.VerificationToken;

public interface VerificationTokenService {

    VerificationToken createVerificationToken(User user);
    User validateVerificationToken(String token);
    void deleteToken(String token);

}
