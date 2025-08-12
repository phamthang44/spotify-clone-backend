package com.thang.spotify.service.validator;

import com.thang.spotify.common.util.Util;
import com.thang.spotify.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {

    public void validateEmail(String email) {
        if (Util.isNullOrEmpty(email)) {
            log.error("Email is null or empty");
            throw new InvalidDataException("Email is required");
        }
        if (Util.isInvalidEmailFormat(email)) {
            log.error("Invalid email format");
            throw new InvalidDataException("Invalid email format");
        }
        if (Util.isInvalidEmailLength(email)) {
            log.error("Invalid email length");
            throw new InvalidDataException("Email must not exceed 255 characters");
        }
    }

    public void validatePassword(String password) {
        if (Util.isNullOrEmpty(password)) {
            log.error("Password is null or empty");
            throw new InvalidDataException("Password is required");
        }
        if (Util.isInvalidPasswordFormat(password)) {
            log.error("Invalid password format: {}", password);
            throw new InvalidDataException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
        }
    }

    public void validateDisplayName(String displayName) {
        if (Util.isNullOrEmpty(displayName)) {
            log.error("Username cannot be null or empty");
            throw new InvalidDataException("Username is required");
        }
    }

}
