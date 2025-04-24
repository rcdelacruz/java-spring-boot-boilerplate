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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public AuthResponseDto authenticate(AuthRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ValidationException("User not found"));
        
        String token = jwtTokenProvider.generateToken(userDetails);
        
        return AuthResponseDto.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthResponseDto register(UserDto userDto) {
        // Create a new user
        UserDto createdUser = userService.createUser(userDto);
        
        // Generate authentication token
        AuthRequestDto authRequest = new AuthRequestDto(userDto.getUsername(), userDto.getPassword());
        return authenticate(authRequest);
    }
}