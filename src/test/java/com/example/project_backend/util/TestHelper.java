package com.example.project_backend.util;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.model.AbstractEntity;
import com.example.project_backend.model.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Component
@Slf4j
public class TestHelper {

    public static final String[] ENTITY_FIELDS_IGNORE = new String[]{AbstractEntity.Fields.id,
            AbstractEntity.Fields.createdAt, AbstractEntity.Fields.updatedAt, AbstractEntity.Fields.deletedAt,
            TaskDto.Fields.userId, User.Fields.password, User.Fields.tasks};

    @Autowired
    private ObjectMapper objectMapper;

    public static PostgreSQLContainer<?> postgreSQLContainer() {
        log.info("Starting PostgreSQL container");
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));
    }

    public static void postgreSqlProperties(final DynamicPropertyRegistry registry,
                                            final PostgreSQLContainer<?> postgreSQLContainer) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        log.info("Ending DynamicPropertyRegistry");
    }

    public String toJsonWithPassword(UserDto userDto) {
        ObjectNode json = objectMapper.convertValue(userDto, ObjectNode.class);
        json.put("password", userDto.getPassword());
        return json.toString();
    }

}

