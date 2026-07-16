package com.example.project_backend.controller;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.mapper.TaskMapper;
import com.example.project_backend.model.task.Task;
import com.example.project_backend.repository.TaskRepository;
import com.example.project_backend.util.DataGenerate;
import com.example.project_backend.util.TestHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests by MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration-test")
@AutoConfigureWireMock(port = 0)
class TaskControllerTest {

    private static final String BASE_URL = "/api/tasks";
    private static final String BASE_URL_WITH_ID = BASE_URL + "/{id}";
    private static final String URL_WITHOUT_SECURE = BASE_URL + "/without-secure/%s";

    @Container
    public static PostgreSQLContainer postgreSQLContainer = TestHelper.postgreSQLContainer();

    @DynamicPropertySource
    static void datasourceProperties(final DynamicPropertyRegistry registry) {
        TestHelper.postgreSqlProperties(registry, postgreSQLContainer);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataGenerate dataGenerate;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskRepository taskRepository;

    @SneakyThrows
    @Test
    @DisplayName("Successfully getAll the tasks")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testGetAllTasks_Success() {
        final Task task_1 = dataGenerate.saveTask();
        final Task task_2 = dataGenerate.saveTask();

        ResultActions resultActions = mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());

        List<TaskDto> expectDtos = objectMapper.readValue(resultActions
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        new TypeReference<List<TaskDto>>() {
                        }).stream()
                .filter(f -> f.getId().equals(task_1.getId()) || f.getId().equals(task_2.getId()))
                .toList();

        assertThat(List.of(task_1, task_2))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(taskMapper.toEntities(expectDtos));
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully create the task")
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void testTaskCreate_Success() {
        final TaskDto taskDto = dataGenerate.getTaskDto();

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isOk());

        TaskDto expectDto = objectMapper.readValue(resultActions
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                TaskDto.class);

        assertThat(expectDto)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(taskDto);
    }


    @SneakyThrows
    @Test
    @DisplayName("Successfully update the task")
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void testTaskUpdate_Success() {
        final Task oldTask = dataGenerate.saveTask();
        final TaskDto taskDto = dataGenerate.getTaskDto();
        taskDto.setId(null);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL_WITH_ID, oldTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isOk());

        TaskDto expectDto = objectMapper.readValue(resultActions
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                TaskDto.class);

        assertThat(expectDto)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(taskDto);
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully get the task")
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void testGetTask_Success() {
        final Task task = dataGenerate.saveTask();

        ResultActions resultActions = mockMvc.perform(get(BASE_URL_WITH_ID, task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        TaskDto expectDto = objectMapper.readValue(resultActions
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                TaskDto.class);

        assertThat(expectDto)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(task);
    }

    @SneakyThrows
    @Test
    @DisplayName("Successfully delete the task")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void testDeleteById_Success() {
        final Task task = dataGenerate.saveTask();

        mockMvc.perform(delete(BASE_URL_WITH_ID, task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(204));

        taskRepository.findById(task.getId()).ifPresent(expectTask ->
                assertThat(expectTask.getDeletedAt()).isNotNull());
    }

    @SneakyThrows
    @Test
    @DisplayName("Reject get the task with bad id")
    void testGetByBadId() {
        final UUID badId = DataGenerate.getAnyId();

        final var url = String.format(URL_WITHOUT_SECURE, badId);

        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())),
                        jsonPath("$.type", not(blankOrNullString())),
                        jsonPath("$.title", not(blankOrNullString())),
                        jsonPath("$.detail", not(blankOrNullString())),
                        jsonPath("$.instance", is(url))
                );
    }

    @SneakyThrows
    @Test
    @DisplayName("Reject delete the task with bad id")
    void testDeleteByBadId() {
        final UUID badId = DataGenerate.getAnyId();

        final var url = String.format(URL_WITHOUT_SECURE, badId);

        mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())),
                        jsonPath("$.type", not(blankOrNullString())),
                        jsonPath("$.title", not(blankOrNullString())),
                        jsonPath("$.detail", not(blankOrNullString())),
                        jsonPath("$.instance", is(url))
                );
    }

    @SneakyThrows
    @Test
    @DisplayName("Reject update the task")
    void testTaskUpdate_BadRequest() {
        final Task oldTask = dataGenerate.saveTask();
        final TaskDto taskDto = dataGenerate.getTaskDto();
        taskDto.setTitle(null);

        final var url = String.format(URL_WITHOUT_SECURE, oldTask.getId());

        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())),
                        jsonPath("$.type", not(blankOrNullString())),
                        jsonPath("$.title", not(blankOrNullString())),
                        jsonPath("$.detail", not(blankOrNullString())),
                        jsonPath("$.instance", is(url))
                );
    }

}