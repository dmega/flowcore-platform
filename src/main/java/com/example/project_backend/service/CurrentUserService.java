package com.example.project_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CurrentUserService {

    public String getCurrentUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Current username={}", username);
        return username;
    }

}
