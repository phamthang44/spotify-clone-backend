package com.thang.spotify.feature.user.service.impl;

import com.thang.spotify.common.enums.*;
import com.thang.spotify.common.exception.*;
import com.thang.spotify.feature.user.mapper.UserMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.feature.auth.dto.request.RegisterRequest;
import com.thang.spotify.feature.user.dto.response.UserResponse;
import com.thang.spotify.feature.auth.entity.Role;
import com.thang.spotify.feature.user.entity.User;
import com.thang.spotify.feature.auth.entity.VerificationToken;
import com.thang.spotify.infra.configuration.AppProperties;
import com.thang.spotify.infra.email.EmailService;
import com.thang.spotify.feature.auth.repository.RoleRepository;
import com.thang.spotify.feature.user.repository.UserRepository;
import com.thang.spotify.feature.asset.service.UserAssetService;
import com.thang.spotify.feature.user.service.UserService;
import com.thang.spotify.feature.auth.service.VerificationTokenService;
import com.thang.spotify.feature.user.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;


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
    private final UserAssetService userAssetService;
    private final AppProperties appProperties;

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
        Long userId = userRepository.save(user).getId();
        VerificationToken token = verificationTokenService.createVerificationToken(user);

        if (userId != null) {
            log.info("User registered successfully with ID: {}", userId);
            try {
                String link = appProperties.getFrontendUrl() + "/verify?token=" + token.getToken();
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

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public long completeOAuth2Signup(User user, RegisterRequest registerRequestDTO) {
        String email = registerRequestDTO.getEmail().trim();
        long completedUserId = 0;
        if (user.getStatus() == UserStatus.INCOMPLETE) {
            log.error("User status is not INCOMPLETE");
            user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            user.setDisplayName(registerRequestDTO.getDisplayName());
            user.setGender(registerRequestDTO.getGender() != null ? registerRequestDTO.getGender() : Gender.OTHER);
            user.setDateOfBirth(registerRequestDTO.getDateOfBirth());
            user.setStatus(UserStatus.ACTIVE);
            completedUserId = userRepository.save(user).getId();
        }

        return completedUserId;
    }

    @Override
    @Transactional
    public void resendVerificationEmail(String email) {
        String emailTrimmed = email.trim();
        userValidator.validateEmail(emailTrimmed);

        User user = userRepository.findByEmail(emailTrimmed)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + emailTrimmed + " not found"));
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new BadRequestException("Email is already verified");
        }
        VerificationToken token = verificationTokenService.createVerificationToken(user);
        try {
            String link = "http://localhost:3000/verify?token=" + token.getToken();
            emailService.sendEmailRegistrationHtml(user.getEmail(), user.getDisplayName(), link);
            log.info("Verification email resent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to resend verification email to {}", user.getEmail(), e);
            throw new CustomMessagingException(ErrorCode.INTERNAL_ERROR, "Failed to resend verification email");
        }
    }

    @Override
    public User getUserById(Long id) {
        Util.validateNumber(id);
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserResponse getUserResponse(Long id) {
        Util.validateNumber(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (user != null) {
            UserResponse response = userMapper.toUserResponse(user);
            String userAvatarUrl = userAssetService.getAvatarUrl(user.getId(), UserAssetType.AVATAR);
            if (Util.isNullOrBlank(userAvatarUrl)) {
                userAvatarUrl = "";
            }
            response.setAvatarUrl(userAvatarUrl);
            return response;
        }

        throw new ResourceNotFoundException("User not found with id: " + id);
    }
}
