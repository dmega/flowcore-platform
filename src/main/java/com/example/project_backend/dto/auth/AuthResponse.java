package com.example.project_backend.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AuthResponse {

    private UUID id;
    private String username;
    private String token;

}
