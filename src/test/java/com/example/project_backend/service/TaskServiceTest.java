package com.example.project_backend.service;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.exception.ResourceNotFoundException;
import com.example.project_backend.mapper.TaskMapper;
import com.example.project_backend.model.task.Task;
import com.example.project_backend.repository.TaskRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * <strong>Unit tests</strong> {@link TaskService}.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;
    @InjectMocks
    private DataGenerate dataGenerate;
    @Spy
    private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
    @Mock
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Successfully getAll the tasks")
    void testGetAllTasks_Success() {
        final var taskDto_1 = dataGenerate.getTaskDto();
        final var taskDto_2 = dataGenerate.getTaskDto();
        List<TaskDto> expectedDtos = List.of(taskDto_1, taskDto_2);

        List<Task> tasks = taskMapper.toEntities(expectedDtos);
        when(taskRepository.findAll()).thenReturn(tasks);
        List<TaskDto> actualDtos = taskService.getAll();

        assertThat(actualDtos)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("Successfully get the task")
    void testGetTaskById_Success() {
        final var anyId = DataGenerate.getAnyId();
        final var expectedDto = dataGenerate.getTaskDto(anyId);

        Task task = taskMapper.createEntity(expectedDto);
        when(taskRepository.findById(anyId)).thenReturn(Optional.of(task));
        TaskDto actualDto = taskService.getById(anyId);

        assertThat(actualDto)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Successfully create the task")
    void testCreateTask_Success() {
        final var expectedDto = dataGenerate.getTaskDto();

        Task task = taskMapper.createEntity(expectedDto);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskDto actualDto = taskService.create(expectedDto);

        assertThat(actualDto)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Successfully update the task")
    void testUpdateTask_Success() {
        final var oldTaskDto = dataGenerate.getTaskDto(DataGenerate.getAnyId());
        final var expectedDto = dataGenerate.getTaskDto();

        Task task = taskMapper.createEntity(oldTaskDto);
        task.setId(oldTaskDto.getId());
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        TaskDto actualDto = taskService.update(task.getId(), expectedDto);

        assertThat(actualDto)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Successfully delete the task")
    void testDeleteTaskById_Success() {
        final var expectedDto = dataGenerate.getTaskDto(DataGenerate.getAnyId());

        Task task = taskMapper.createEntity(expectedDto);
        task.setId(expectedDto.getId());
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        taskService.deleteById(task.getId());

        assertThat(task.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Reject get the task with bad id")
    void testGetTaskById_NotFound() {
        final var badId = DataGenerate.getAnyId();

        when(taskRepository.findById(badId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getById(badId));
    }

    @Test
    @DisplayName("Reject delete the task with bad id")
    void testDeleteByBadId_NotFound() {
        final var badId = DataGenerate.getAnyId();

        when(taskRepository.findById(badId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteById(badId));
    }

}
