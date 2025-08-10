package com.thang.spotify.service.impl.security;

import com.thang.spotify.entity.User;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = (User) userRepository
                .findByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        List<GrantedAuthority> authorityList = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase())
        );

        authorityList.forEach(authority -> log.info("Authority: {}", authority));

        return SecurityUserDetails.build(user, authorityList);
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Optional.empty();

        Object principal = authentication.getPrincipal();
        log.info("Principal: {}", principal);

        if (principal instanceof SecurityUserDetails securityUserDetails) {
            // Return the already fetched user from the authentication context
            return Optional.of(securityUserDetails.getUser());
        }

        return Optional.empty();
    }
}
