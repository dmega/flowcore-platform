package com.example.project_backend.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

public interface AbstractMapper<Entity, Dto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Entity createEntity(Dto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(Dto source, @MappingTarget Entity target);

    Dto toDto(Entity source);

    List<Entity> toEntities(List<Dto> source);

    List<Dto> toDtos(List<Entity> source);

}
