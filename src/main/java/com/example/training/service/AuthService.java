package com.example.training.service;

import com.example.training.dto.AuthRequestDto;
import com.example.training.dto.AuthResponseDto;
import com.example.training.dto.UserDto;

public interface AuthService {
    AuthResponseDto authenticate(AuthRequestDto request);
    
    AuthResponseDto register(UserDto userDto);
}