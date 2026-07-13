package com.example.project_backend.api;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Validated
@Tag(name = "task", description = "API Tasks")
public interface TaskApi {

    @Operation(
            summary = "Create Task",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = BadRequestException.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Permission denied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Exception.class))
                    })
            }
    )
    ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto);

    @Operation(
            summary = "Get all Tasks",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = BadRequestException.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Permission denied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Exception.class))
                    })
            }
    )
    ResponseEntity<List<TaskDto>> getAllTasks();

    @Operation(
            summary = "Update Task",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = BadRequestException.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Permission denied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Exception.class))
                    })
            }
    )
    ResponseEntity<TaskDto> update(@PathVariable UUID id, @Valid @RequestBody TaskDto taskDto);

    @Operation(
            summary = "Get Task by Id",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = BadRequestException.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Permission denied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Exception.class))
                    })
            }
    )
    ResponseEntity<TaskDto> getTask(@PathVariable UUID id);

    @Operation(
            summary = "Delete Task",
            tags = {"task"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = BadRequestException.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Permission denied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Exception.class))
                    })
            }
    )
    ResponseEntity<Void> deleteById(@PathVariable UUID id);

}
