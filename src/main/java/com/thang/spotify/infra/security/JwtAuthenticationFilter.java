package com.thang.spotify.infra.security;

import com.thang.spotify.common.enums.ErrorCode;
import com.thang.spotify.common.exception.TokenExpiredException;
import com.thang.spotify.common.exception.TokenNotFoundException;
import com.thang.spotify.feature.auth.service.impl.JwtTokenService;
import com.thang.spotify.feature.auth.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        token = authHeader.substring(7);
        try {
            username = jwtTokenService.extractUsername(token);
        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired: {}", ex.getMessage());
            throw new TokenExpiredException(ErrorCode.INVALID_TOKEN, "JWT Token expired");
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Invalid JWT: {}", ex.getMessage());
            throw new TokenNotFoundException(ErrorCode.INVALID_TOKEN, "Invalid JWT");
        }
        log.info("Validate JWT username: {}", username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityUserDetails userDetails = (SecurityUserDetails) userDetailsService.loadUserByUsername(username);
            if (jwtTokenService.isValid(token, userDetails)) {
                log.info("Validate JWT username: success");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
//        else {
//            SecurityContextHolder.clearContext();
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"status\":false,\"message\":\"Token is invalid or expired\"}");
//        }

        filterChain.doFilter(request, response);
    }
}