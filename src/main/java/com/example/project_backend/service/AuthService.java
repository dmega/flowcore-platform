package com.example.project_backend.service;

import com.example.project_backend.dto.auth.AuthRequest;
import com.example.project_backend.dto.auth.AuthResponse;
import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.security.JwtTokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserServiceHelper serviceHelper;
    private final JwtTokenService jwtTokenService;

    public AuthResponse login(@NonNull final AuthRequest authRequest) {
        AuthResponse authResponse = new AuthResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()));
        UserDto userDto = serviceHelper.getUserDtoByUsername(authRequest.getUsername());
        authResponse.setId(userDto.getId());
        authResponse.setUsername(userDto.getUsername());
        authResponse.setToken(jwtTokenService.createToken(userDto.getId(), userDto.getUsername(), userDto.getRoles()));

        return authResponse;
    }

}
