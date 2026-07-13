package com.example.project_backend.mapper;

import com.example.project_backend.dto.task.TaskDto;
import com.example.project_backend.model.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper extends AbstractMapper<Task, TaskDto> {

    @Override
    @Mapping(source = "user.id", target = "userId")
    TaskDto toDto(Task source);

    @Override
    List<Task> toEntities(List<TaskDto> source);

    @Override
    List<TaskDto> toDtos(List<Task> source);

}
