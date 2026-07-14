package com.example.project_backend.service;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.exception.BadRequestException;
import com.example.project_backend.exception.ResourceNotFoundException;
import com.example.project_backend.mapper.TaskMapper;
import com.example.project_backend.mapper.UserMapper;
import com.example.project_backend.model.task.Task;
import com.example.project_backend.model.user.User;
import com.example.project_backend.repository.TaskRepository;
import com.example.project_backend.repository.UserRepository;
import com.example.project_backend.util.DataGenerate;
import com.example.project_backend.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * <strong>Unit tests</strong> {@link UserService}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @InjectMocks
    private DataGenerate dataGenerate;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Spy
    private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Successfully getAll the users")
    void testGetAllUsers_Success() {
        final var userDto_1 = dataGenerate.getUserDto();
        final var userDto_2 = dataGenerate.getUserDto();
        List<UserDto> expectedDtos = List.of(userDto_1, userDto_2);

        List<User> users = userMapper.toEntities(expectedDtos);
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> actualDtos = userService.getAll();

        assertThat(actualDtos)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("Successfully get the user")
    void testGetUserById_Success() {
        final var anyId = dataGenerate.getAnyId();
        final var expectedDto = dataGenerate.getUserDto(anyId);

        User user = userMapper.createEntity(expectedDto);
        when(userRepository.findById(anyId)).thenReturn(Optional.of(user));
        UserDto actualDto = userService.getById(anyId);

        assertThat(actualDto)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Successfully create the user")
    void testCreateUser_Success() {
        final var expectedDto = dataGenerate.getUserDto();

        User user = userMapper.createEntity(expectedDto);
        when(passwordEncoder.encode(expectedDto.getPassword())).thenReturn(expectedDto.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto actualDto = userService.createUser(expectedDto);

        assertThat(actualDto)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Successfully update the user")
    void testUpdateUser_Success() {
        final var oldUserDto = dataGenerate.getUserDto(dataGenerate.getAnyId());
        final var expectedDto = dataGenerate.getUserDto();

        User user = userMapper.createEntity(oldUserDto);
        user.setId(oldUserDto.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserDto actualDto = userService.update(user.getId(), expectedDto);

        assertThat(actualDto)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Successfully delete the user")
    void testDeleteUserById_Success() {
        final var expectedDto = dataGenerate.getUserDto(dataGenerate.getAnyId());

        User user = userMapper.createEntity(expectedDto);
        user.setId(expectedDto.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteById(user.getId());

        assertThat(user.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Successfully get user tasks")
    void testGetUserTasks() {
        final var userDto = dataGenerate.getUserDto(dataGenerate.getAnyId());
        final var taskDto = dataGenerate.getTaskDto();
        taskDto.setUserId(userDto.getId());

        List<TaskDto> expectedDtos = List.of(taskDto);
        Task task = taskMapper.createEntity(taskDto);
        when(taskRepository.findAllByUserId(userDto.getId())).thenReturn(List.of(task));

        List<TaskDto> actualDtos = userService.getUserTasks(userDto.getId());

        assertThat(actualDtos)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("Reject get the user with bad id")
    void testGetUserById_NotFound() {
        final var badId = DataGenerate.getAnyId();

        when(userRepository.findById(badId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(badId));
    }

    @Test
    @DisplayName("Reject create the user")
    void testCreateUser_Exception() {
        final var userDto = dataGenerate.getUserDto();
        userDto.setEmail(null);

        when(userRepository.save(any(User.class))).thenThrow(new BadRequestException("Email is required"));

        assertThrows(BadRequestException.class, () -> userService.createUser(userDto));
    }

}