package com.example.project_backend.service;

import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.exception.ResourceNotFoundException;
import com.example.project_backend.mapper.UserMapper;
import com.example.project_backend.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserServiceHelper {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional(readOnly = true)
    public UserDto getUserDtoByUsername(@NonNull String username) {
        return repository.findByUsername(username).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
