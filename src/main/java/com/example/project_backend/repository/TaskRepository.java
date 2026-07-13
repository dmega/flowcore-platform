package com.example.project_backend.repository;

import com.example.project_backend.model.task.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends SourceRepository<Task, UUID> {

    List<Task> findAllByUserId(UUID userId);

}
