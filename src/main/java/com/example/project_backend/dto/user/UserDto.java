package com.example.project_backend.dto.user;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.model.user.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Schema(name = "UserDto", description = "User")
@Accessors(chain = true)
public class UserDto implements Serializable {

    @Schema(
            name = "ID",
            example = "11111111-1111-1111-1111-111111111111",
            description = "User id",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private UUID id;

    @Schema(
            name = "Username",
            example = "alice", description = "Username",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @NotNull(message = "Username is required")
    @Length(max = 255, message = "Username can contain a maximum of 255 symbols")
    private String username;

    @Schema(
            name = "Email",
            example = "alice@example.com",
            description = "User email",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @NotNull(message = "Email is required")
    @Length(max = 255, message = "Email can contain a maximum of 255 symbols")
    private String email;

    @Schema(
            name = "Password",
            example = "$2a$10$examplePasswordHash2m",
            description = "User crypted password",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password is required")
    private String password;

    @Schema(
            name = "Role",
            example = "USER",
            description = "User role",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @NotNull(message = "Role is required")
    @ToString.Exclude
    private Set<UserRole> roles;

    @Schema(
            name = "Task",
            example = "DONE",
            description = "User tasks",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ToString.Exclude
    private List<TaskDto> tasks;

}