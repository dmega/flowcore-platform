package com.example.project_backend.controller;

import com.example.project_backend.api.TaskApi;
import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.service.TaskService;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController implements TaskApi {

    private final TaskService taskService;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskDto> createTask(final TaskDto taskDto) {
        log.debug("createTask taskDto={}", taskDto);
        return ResponseEntity.ok(taskService.create(taskDto));
    }

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        log.debug("getAllTasks");
        return ResponseEntity.ok(taskService.getAll());
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskDto> update(@NonNull final UUID id, final TaskDto taskDto) {
        log.debug("updateTask id={}, taskDto={}", id, taskDto);
        return ResponseEntity.ok(taskService.update(id, taskDto));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskDto> getTask(@NonNull final UUID id) {
        log.debug("getTask id={}", id);
        return ResponseEntity.ok(taskService.getById(id));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@NonNull final UUID id) {
        log.debug("deleteById id={}", id);
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Without secure for testing
     */

    @GetMapping("/without-secure/{id}")
    public ResponseEntity<TaskDto> getById_test(@NonNull @PathVariable final UUID id) {
        return getTask(id);
    }

    @DeleteMapping("/without-secure/{id}")
    public ResponseEntity<Void> deleteById_test(@NonNull @PathVariable final UUID id) {
        return deleteById(id);
    }

    @PutMapping("/without-secure/{id}")
    public ResponseEntity<TaskDto> createTask_test(@NonNull @PathVariable final UUID id,
                                                   @Valid @RequestBody final TaskDto taskDto) {
        return update(id, taskDto);
    }

}
