package com.example.project_backend.repository;

import com.example.project_backend.model.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends SourceRepository<User, UUID> {

    Optional<User> findByUsername(String username);

}
