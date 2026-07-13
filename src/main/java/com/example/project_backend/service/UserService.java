package com.example.project_backend.service;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.exception.BadRequestException;
import com.example.project_backend.mapper.TaskMapper;
import com.example.project_backend.mapper.UserMapper;
import com.example.project_backend.model.user.User;
import com.example.project_backend.repository.TaskRepository;
import com.example.project_backend.repository.UserRepository;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService extends AbstractService<UUID, User, UserDto, UserRepository, UserMapper> {

    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskMapper taskMapper;
    private final CurrentUserService currentUserService;
    private final UserServiceHelper serviceHelper;

    public UserService(final UserRepository repository, final UserMapper mapper, final TaskRepository taskRepository,
                       final PasswordEncoder passwordEncoder, final TaskMapper taskMapper,
                       final CurrentUserService currentUserService, final UserServiceHelper serviceHelper) {
        super(repository, mapper);
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskMapper = taskMapper;
        this.currentUserService = currentUserService;
        this.serviceHelper = serviceHelper;
    }

    public UserDto createUser(@NonNull final UserDto userdto) {
        if (repository.findByUsername(userdto.getUsername()).isPresent()) {
            throw new BadRequestException("User already exist");
        }
        userdto.setPassword(passwordEncoder.encode(userdto.getPassword()));
        return super.create(userdto);
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        String username = currentUserService.getCurrentUsername();
        return serviceHelper.getUserDtoByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getUserTasks(@NonNull final UUID id) {
        return taskMapper.toDtos(taskRepository.findAllByUserId(id));
    }

}
