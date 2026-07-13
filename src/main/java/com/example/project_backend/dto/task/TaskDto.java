package com.example.project_backend.dto.task;

import com.example.project_backend.model.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.UUID;

@Data
@Schema(name = "TaskDTO", description = "Task")
@Accessors(chain = true)
@FieldNameConstants
public class TaskDto implements Serializable {

    @Schema(
            name = "ID",
            example = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
            description = "Task id",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private UUID id;

    @Schema(
            name = "Title",
            example = "Implement JWT authentication",
            description = "Task title",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Title is required")
    @Length(max = 255, message = "Title can contain a maximum of 255 symbols")
    private String title;

    @Schema(
            name = "Description",
            example = "Implement JWT authentication using Spring Security",
            description = "Task description",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Length(max = 255, message = "Description can contain a maximum of 255 symbols")
    private String description;

    @Schema(
            name = "Status",
            example = "TODO",
            description = "Current task status",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private TaskStatus status;

    @Schema(
            name = "User_id",
            description = "Task owner",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID userId;

}

