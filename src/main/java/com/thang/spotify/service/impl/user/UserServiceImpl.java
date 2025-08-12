package com.thang.spotify.service.impl.user;

import com.thang.spotify.common.enums.ErrorCode;
import com.thang.spotify.common.enums.Gender;
import com.thang.spotify.common.enums.UserStatus;
import com.thang.spotify.common.mapper.UserMapper;
import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.entity.Role;
import com.thang.spotify.entity.User;
import com.thang.spotify.exception.*;
//import com.thang.spotify.infra.email.EmailService;
import com.thang.spotify.repository.other.RoleRepository;
import com.thang.spotify.repository.user.UserRepository;
import com.thang.spotify.service.user.UserService;
import com.thang.spotify.service.validator.UserValidator;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.thang.spotify.common.enums.UserType;
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
//    private final EmailService emailService;

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
        user.setStatus(UserStatus.ACTIVE);
        user.setGender(gender);
        user.setDateOfBirth(dateOfBirth);

        Long userId = userRepository.save(user).getId();
        if (userId != null) {
            log.info("User registered successfully with ID: {}", userId);
            try {
//                emailService.sendEmailRegistrationHtml(user.getEmail(), user.getDisplayName());
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

}
