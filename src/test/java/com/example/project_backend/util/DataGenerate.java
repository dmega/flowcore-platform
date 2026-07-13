package com.example.project_backend.util;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.mapper.TaskMapper;
import com.example.project_backend.mapper.UserMapper;
import com.example.project_backend.model.task.Task;
import com.example.project_backend.model.task.TaskStatus;
import com.example.project_backend.model.user.User;
import com.example.project_backend.model.user.UserRole;
import com.example.project_backend.repository.TaskRepository;
import com.example.project_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataGenerate {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    public TaskDto getTaskDto() {
        return getTaskDto(null);
    }

    public TaskDto getTaskDto(UUID id) {
        return new TaskDto()
                .setId(id != null ? id : null)
                .setTitle("title_" + getAnyId())
                .setDescription("description_" + getAnyId())
                .setStatus(TaskStatus.TODO);
    }

    public UserDto getUserDto() {
        return getUserDto(null);
    }

    public UserDto getUserDto(UUID id) {
        return new UserDto()
                .setId(id != null ? id : null)
                .setUsername("username_" + getAnyId())
                .setEmail("user@email_" + getAnyId())
                .setPassword("password_" + getAnyId())
                .setRoles(Set.of(UserRole.ROLE_USER));
    }

    @Transactional
    public Task saveTask() {
        return taskRepository.save(taskMapper.createEntity(getTaskDto()));
    }

    @Transactional
    public Task setUser(User user) {
        Task task = taskMapper.createEntity(getTaskDto());
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional
    public User saveUser() {
        return userRepository.save(userMapper.createEntity(getUserDto()));
    }

    public static UUID getAnyId() {
        return UUID.randomUUID();
    }

}
