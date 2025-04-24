package com.example.training.service.impl;

import com.example.training.dto.AuthRequestDto;
import com.example.training.dto.AuthResponseDto;
import com.example.training.dto.UserDto;
import com.example.training.exception.ValidationException;
import com.example.training.model.User;
import com.example.training.repository.UserRepository;
import com.example.training.security.JwtTokenProvider;
import com.example.training.service.AuthService;
import com.example.training.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public AuthResponseDto authenticate(AuthRequestDto request) {
        try {
            log.debug("Attempting to authenticate user: {}", request.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            log.debug("User authenticated successfully: {}", userDetails.getUsername());

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ValidationException("User not found"));

            String token = jwtTokenProvider.generateToken(userDetails);
            log.debug("JWT token generated successfully for user: {}", userDetails.getUsername());

            return AuthResponseDto.builder()
                    .token(token)
                    .username(user.getUsername())
                    .userId(user.getId())
                    .build();
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", request.getUsername());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during authentication for user: {}", request.getUsername(), e);
            throw e;
        }
    }

    @Override
    public AuthResponseDto register(UserDto userDto) {
        try {
            log.debug("Attempting to register new user: {}", userDto.getUsername());

            // Create a new user
            UserDto createdUser = userService.createUser(userDto);
            log.debug("User created successfully: {}", createdUser.getUsername());

            // Generate authentication token
            AuthRequestDto authRequest = new AuthRequestDto(userDto.getUsername(), userDto.getPassword());
            log.debug("Authenticating newly registered user: {}", userDto.getUsername());

            return authenticate(authRequest);
        } catch (Exception e) {
            log.error("Error during user registration: {}", userDto.getUsername(), e);
            throw e;
        }
    }
}
