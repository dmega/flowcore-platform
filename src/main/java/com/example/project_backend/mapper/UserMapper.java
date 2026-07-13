package com.example.project_backend.mapper;

import com.example.project_backend.dto.user.UserDto;
import com.example.project_backend.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends AbstractMapper<User, UserDto> {

    @Override
    UserDto toDto(User source);

    @Override
    List<User> toEntities(List<UserDto> source);

    @Override
    List<UserDto> toDtos(List<User> source);

}
