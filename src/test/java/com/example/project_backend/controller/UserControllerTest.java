package com.example.project_backend.controller;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.mapper.TaskMapper;
import com.example.project_backend.mapper.UserMapper;
import com.example.project_backend.model.task.Task;
import com.example.project_backend.model.user.User;
import com.example.project_backend.repository.UserRepository;
import com.example.project_backend.service.AuthenticationService;
import com.example.project_backend.util.DataGenerate;
import com.example.project_backend.util.TestHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests by RestAssured.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("integration-test")
public class UserControllerTest {

    private static final String BASE_URL = "/api/users";
    private static final String BASE_URL_WITH_ID = BASE_URL + "/{id}";
    private static final String URL_WITHOUT_SECURE = BASE_URL + "/without-secure";
    private static final String URL_WITHOUT_SECURE_WITH_ID = URL_WITHOUT_SECURE + "/%s";

    @Container
    public static PostgreSQLContainer postgreSQLContainer = TestHelper.postgreSQLContainer();

    @DynamicPropertySource
    static void datasourceProperties(final DynamicPropertyRegistry registry) {
        TestHelper.postgreSqlProperties(registry, postgreSQLContainer);
    }

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DataGenerate dataGenerate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TestHelper testHelper;
    @Autowired
    private UserRepository userRepository;

    private String accessToken;

    @BeforeEach
    void init(@LocalServerPort int port) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        accessToken = "Bearer " + authenticationService.getToken();
    }

    @Test
    @DisplayName("Successfully getAll the users")
    void testGetAllUsers_Success() {
        final User user_1 = dataGenerate.saveUser();
        final User user_2 = dataGenerate.saveUser();

        List<UserDto> expectDtos = RestAssured.given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", UserDto.class)
                .stream()
                .filter(f -> f.getId().equals(user_1.getId()) || f.getId().equals(user_2.getId()))
                .toList();

        assertThat(List.of(user_1, user_2))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(userMapper.toEntities(expectDtos));
    }

    @Test
    @DisplayName("Successfully get the user")
    void testGetUserById_Success() {
        final User user = dataGenerate.saveUser();

        UserDto expectedUserDto = RestAssured.given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL_WITH_ID, user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", UserDto.class);

        assertThat(user)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(userMapper.createEntity(expectedUserDto));
    }

    @Test
    @DisplayName("Successfully create the user")
    void testCreateUser_Success() {
        final UserDto userDto = dataGenerate.getUserDto();

        UserDto expectedUserDto = RestAssured.given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(testHelper.toJsonWithPassword(userDto))
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", UserDto.class);

        assertThat(userDto)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedUserDto);
    }

    @Test
    @DisplayName("Successfully update the user")
    void testUpdateUser_Success() {
        final User user = dataGenerate.saveUser();
        final UserDto userDto = dataGenerate.getUserDto();
        userDto.setId(null);

        UserDto expectedUserDto = RestAssured.given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(testHelper.toJsonWithPassword(userDto))
                .when()
                .put(BASE_URL_WITH_ID, user.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", UserDto.class);

        assertThat(userDto)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(TestHelper.ENTITY_FIELDS_IGNORE)
                .isEqualTo(expectedUserDto);
    }

    @Test
    @DisplayName("Successfully delete the user")
    void testDeleteUserById_Success() {
        final User user = dataGenerate.saveUser();

        RestAssured.given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete(BASE_URL_WITH_ID, user.getId())
                .then()
                .statusCode(HttpStatus.valueOf(204).value());

        userRepository.findById(user.getId()).ifPresent(expectUser ->
                assertThat(expectUser.getDeletedAt()).isNotNull());
    }

    @Test
    @DisplayName("Successfully get current user")
    void testGetCurrentUser() {
        final var url = BASE_URL + "/current";

        UserDto expectedUserDto = RestAssured.given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", UserDto.class);

        String username = authenticationService.getUsername();

        assertThat(username).isEqualTo(expectedUserDto.getUsername());
    }

    @Test
    @DisplayName("Successfully get user tasks")
    void testGetUserTasks() {
        final User user = dataGenerate.saveUser();
        final Task task = dataGenerate.setUser(user);

        final var url = String.format(BASE_URL + "/%s/tasks", user.getId());

        List<TaskDto> expectDtos = RestAssured.given()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", TaskDto.class);

        assertThat(taskMapper.toDtos(List.of(task)))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectDtos);
    }

    @Test
    @DisplayName("Reject get the user with bad id")
    void testGetUserById_NotFound() {
        final UUID badId = DataGenerate.getAnyId();

        final var url = String.format(URL_WITHOUT_SECURE_WITH_ID, badId);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType("application/problem+json")
                .body("status", is(HttpStatus.NOT_FOUND.value()))
                .body("type", not(blankOrNullString()))
                .body("title", not(blankOrNullString()))
                .body("detail", not(blankOrNullString()))
                .body("instance", is(url));
    }

    @SneakyThrows
    @Test
    @DisplayName("Reject create the user")
    void testCreateUser_Exception() {
        final UserDto userDto = dataGenerate.getUserDto();
        userDto.setEmail(null);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(testHelper.toJsonWithPassword(userDto))
                .when()
                .post(URL_WITHOUT_SECURE)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType("application/problem+json")
                .body("status", is(HttpStatus.BAD_REQUEST.value()))
                .body("type", not(blankOrNullString()))
                .body("title", not(blankOrNullString()))
                .body("detail", not(blankOrNullString()))
                .body("instance", is(URL_WITHOUT_SECURE));
    }

}