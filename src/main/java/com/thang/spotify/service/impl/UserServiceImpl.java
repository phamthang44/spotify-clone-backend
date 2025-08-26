package com.thang.spotify.service.impl;

import com.thang.spotify.common.enums.*;
import com.thang.spotify.common.mapper.UserMapper;
import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.entity.Role;
import com.thang.spotify.entity.User;
import com.thang.spotify.entity.VerificationToken;
import com.thang.spotify.exception.*;
//import com.thang.spotify.infra.email.EmailService;
import com.thang.spotify.infra.email.EmailService;
import com.thang.spotify.repository.RoleRepository;
import com.thang.spotify.repository.UserRepository;
import com.thang.spotify.service.UserService;
import com.thang.spotify.service.VerificationTokenService;
import com.thang.spotify.service.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;

    @Transactional
    @Override
    public long registerUser(RegisterRequest registerRequestDTO) {

        if (registerRequestDTO == null) {
            log.error("Register request is null");
            throw new BadRequestException("Register request cannot be null");
        }

        log.info("Registering user with email: {}", registerRequestDTO.getEmail());
        // Save user to the repository and return user ID

        String email = registerRequestDTO.getEmail().trim();
        String displayName = registerRequestDTO.getDisplayName().trim();
        String password = registerRequestDTO.getPassword().trim();

        userValidator.validateEmail(email);
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists");
            throw new DuplicateResourceException("Email is already registered");
        }

        userValidator.validatePassword(password);

        userValidator.validateDisplayName(displayName);

        Gender gender = registerRequestDTO.getGender() != null ? registerRequestDTO.getGender() : Gender.OTHER;
        LocalDate dateOfBirth = registerRequestDTO.getDateOfBirth();


        Role role = roleRepository.findByName(UserType.LISTENER.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        User user = userMapper.toUser(registerRequestDTO);
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setStatus(UserStatus.UNVERIFIED);
        user.setGender(gender);
        user.setDateOfBirth(dateOfBirth);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setProviderId(null);
        VerificationToken token = verificationTokenService.createVerificationToken(user);

        Long userId = userRepository.save(user).getId();
        if (userId != null) {
            log.info("User registered successfully with ID: {}", userId);
            try {
                String link = "http://localhost:3000/verify?token=" + token.getToken();
                emailService.sendEmailRegistrationHtml(user.getEmail(), user.getDisplayName(), link);
                log.info("Registration email sent to {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send registration email to {}", user.getEmail(), e);
                throw new CustomMessagingException(ErrorCode.INTERNAL_ERROR, "Failed to send registration email");
            }
        } else {
            log.error("Failed to register user");
            throw new InternalServerException("Failed to register user");
        }
        return userId;
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        User user = verificationTokenService.validateVerificationToken(token);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        verificationTokenService.deleteToken(token);
    }
}
