package com.thang.spotify.service.impl;

import com.thang.spotify.common.enums.ErrorCode;
import com.thang.spotify.entity.User;
import com.thang.spotify.entity.VerificationToken;
import com.thang.spotify.exception.VerificationTokenException;
import com.thang.spotify.repository.VerificationTokenRepository;
import com.thang.spotify.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken createVerificationToken(User user) {
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24h hết hạn

        return verificationTokenRepository.save(token);
    }

    @Override
    public User validateVerificationToken(String tokenValue) {
        VerificationToken token = verificationTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new VerificationTokenException(ErrorCode.INVALID_TOKEN, "Invalid verification token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new VerificationTokenException(ErrorCode.INVALID_TOKEN, "Expired verification token");
        }

        return token.getUser();
    }

    @Override
    public void deleteToken(String token) {
        verificationTokenRepository.findByToken(token)
                .ifPresent(verificationTokenRepository::delete);
    }
}
