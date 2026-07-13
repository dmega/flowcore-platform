package com.example.project_backend.api;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.dto.user.UserDto;
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
@Tag(name = "user", description = "API Users")
public interface UserApi {

    @Operation(
            summary = "Get Users",
            tags = {"user"},
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
    ResponseEntity<List<UserDto>> getAllUsers();

    @Operation(
            summary = "Get User",
            tags = {"user"},
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
    ResponseEntity<UserDto> getUser(@PathVariable UUID id);

    @Operation(
            summary = "Create User",
            tags = {"user"},
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
    ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userdto);

    @Operation(
            summary = "Update User",
            tags = {"user"},
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
    ResponseEntity<UserDto> update(@PathVariable UUID id, @RequestBody UserDto userDto);

    @Operation(
            summary = "Delete User",
            tags = {"user"},
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
    ResponseEntity<Void> deleteUser(@PathVariable UUID id);

    @Operation(
            summary = "Get current User",
            tags = {"user"},
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
    ResponseEntity<UserDto> getCurrentUser();

    @Operation(
            summary = "Get user Tasks",
            tags = {"user"},
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
    ResponseEntity<List<TaskDto>> getUserTasks(@PathVariable UUID id);


}
