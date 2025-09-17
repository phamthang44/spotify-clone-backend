package com.thang.spotify.feature.auth.service.impl;

import com.thang.spotify.common.enums.ErrorCode;
import com.thang.spotify.feature.auth.entity.RefreshToken;
import com.thang.spotify.feature.user.entity.User;
import com.thang.spotify.common.exception.TokenExpiredException;
import com.thang.spotify.common.exception.TokenNotFoundException;
import com.thang.spotify.feature.auth.repository.RefreshTokenRepository;
import com.thang.spotify.feature.auth.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public String createOrUpdateRefreshToken(User user) {
        refreshTokenRepository.revokeAllByUser(user);

        String rawRefreshToken = UUID.randomUUID().toString();
        String hashRefreshToken = passwordEncoder.encode(rawRefreshToken);

        RefreshToken refreshToken;

        refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(hashRefreshToken);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);
        return rawRefreshToken;
    }


    @Override
    public RefreshToken validateRefreshToken(String rawToken) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByRevokedFalseAndExpiryDateAfter(LocalDateTime.now());
        for (RefreshToken tokenEntity : tokens) {
            if (passwordEncoder.matches(rawToken, tokenEntity.getToken())) {
                return tokenEntity;
            }
        }
        throw new RuntimeException("Validate: Invalid or expired refresh token");
    }

    @Override
    public RefreshToken getRefreshToken(String refreshToken) {
        RefreshToken token = validateRefreshToken(refreshToken);
        if (token == null) {
            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Get: Refresh token not found");
        }
        return token;
    }

    @Override
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(ErrorCode.INVALID_TOKEN, "Revoke : Refresh token has expired");
        }
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}
