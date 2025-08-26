package com.thang.spotify.service;

import com.thang.spotify.entity.User;
import com.thang.spotify.entity.VerificationToken;

public interface VerificationTokenService {

    VerificationToken createVerificationToken(User user);
    User validateVerificationToken(String token);
    void deleteToken(String token);

}
