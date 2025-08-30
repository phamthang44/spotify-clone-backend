package com.thang.spotify.service.impl.security;

import com.thang.spotify.common.enums.ErrorCode;
import com.thang.spotify.entity.User;
import com.thang.spotify.entity.VerificationToken;
import com.thang.spotify.exception.VerificationTokenException;
import com.thang.spotify.repository.UserRepository;
import com.thang.spotify.repository.VerificationTokenRepository;
import com.thang.spotify.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;


    @Override
    @Transactional
    public VerificationToken createVerificationToken(User user) {
        VerificationToken token = new VerificationToken();

        String verificationTokenJwt = jwtTokenService.generateVerificationToken(user);

        token.setToken(verificationTokenJwt);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24h hết hạn

        return verificationTokenRepository.save(token);
    }

    @Override
    public User validateVerificationToken(String tokenValue) {

        String emailFromToken = jwtTokenService.getEmailFromJwtToken(tokenValue);
        log.info("Verification service: Email extracted from token: {}", emailFromToken);
        User user = userRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new VerificationTokenException(ErrorCode.INVALID_TOKEN, "User not found for the token"));

        VerificationToken token = verificationTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new VerificationTokenException(ErrorCode.INVALID_TOKEN, "Invalid verification token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new VerificationTokenException(ErrorCode.INVALID_TOKEN, "Expired verification token");
        }

        if (!isTokenBelongToUser(token, user)) {
            throw new VerificationTokenException(ErrorCode.INVALID_TOKEN, "Token does not belong to the user");
        }

        return token.getUser();
    }

    private boolean isTokenBelongToUser(VerificationToken token, User user) {
        return Objects.equals(token.getUser().getId(), user.getId());
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        verificationTokenRepository.findByToken(token)
                .ifPresent(verificationTokenRepository::delete);
    }
}
