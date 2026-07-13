package com.example.project_backend.service;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.mapper.TaskMapper;
import com.example.project_backend.model.task.Task;
import com.example.project_backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService extends AbstractService<UUID, Task, TaskDto, TaskRepository, TaskMapper> {

    public TaskService(final TaskRepository repository, final TaskMapper mapper) {
        super(repository, mapper);
    }

}
