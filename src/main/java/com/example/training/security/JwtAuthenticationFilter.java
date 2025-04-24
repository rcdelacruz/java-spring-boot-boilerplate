package com.example.training.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Only skip for public endpoints
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/") ||
               path.startsWith("/api/v1/environment") ||
               path.startsWith("/swagger-ui/") ||
               path.equals("/swagger-ui.html") ||
               path.equals("/custom-swagger-ui") ||
               path.equals("/swagger-ui-custom.js") ||
               path.startsWith("/v3/api-docs/") ||
               path.startsWith("/api-docs/") ||
               path.startsWith("/actuator/");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            log.debug("Processing JWT token from Authorization header");
            username = jwtTokenProvider.extractUsername(jwt);
            log.debug("Extracted username from token: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Loading user details for username: {}", username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                    log.debug("JWT token is valid for user: {}", username);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication set in SecurityContext for user: {}", username);
                } else {
                    log.warn("JWT token validation failed for user: {}", username);
                }
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }
}
