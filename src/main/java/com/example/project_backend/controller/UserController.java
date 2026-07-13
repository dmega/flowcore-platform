package com.example.project_backend.controller;

import com.example.project_backend.api.UserApi;
import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.debug("getAllUsers");
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getUser(@NonNull final UUID id) {
        log.debug("getUser id={}", id);
        return ResponseEntity.ok(userService.getById(id));
    }

    @Override
    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserDto> createUser(final UserDto userdto) {
        log.debug("createUser userdto={}", userdto);
        return ResponseEntity.ok(userService.createUser(userdto));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> update(@NonNull final UUID id, final UserDto userDto) {
        log.debug("Update user id={}, userDto={}", id, userDto);
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@NonNull final UUID id) {
        log.debug("deleteUser id={}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser() {
        log.debug("getCurrentUser");
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    @GetMapping("/{id}/tasks")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<TaskDto>> getUserTasks(@NonNull final UUID id) {
        log.debug("getUserTasks userId={}", id);
        return ResponseEntity.ok(userService.getUserTasks(id));
    }

    /**
     * Without secure for testing
     */

    @GetMapping("/without-secure/{id}")
    public ResponseEntity<UserDto> getUser_test(@NonNull @PathVariable final UUID id) {
        return getUser(id);
    }

    @PostMapping("/without-secure")
    public ResponseEntity<UserDto> createUser_test(@Valid @RequestBody final UserDto userdto) {
        return createUser(userdto);
    }

}
