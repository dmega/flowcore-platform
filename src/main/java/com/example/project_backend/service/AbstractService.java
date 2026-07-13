package com.example.project_backend.service;

import com.example.project_backend.exception.ResourceNotFoundException;
import com.example.project_backend.mapper.AbstractMapper;
import com.example.project_backend.model.AbstractEntity;
import com.example.project_backend.repository.SourceRepository;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractService<ID, Entity extends AbstractEntity, Dto,
        Repository extends SourceRepository<Entity, ID>,
        Mapper extends AbstractMapper<Entity, Dto>> {

    protected final Repository repository;
    protected final Mapper mapper;

    @Transactional
    public Dto create(@NonNull final Dto dto) {
        log.debug("create entity dto={}", dto);

        Entity entity = mapper.createEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(entity.getCreatedAt());
        Entity result = repository.save(entity);

        return mapper.toDto(result);
    }

    @Transactional
    public Dto update(@NonNull final ID id, @NonNull final Dto dto) {
        log.debug("update id={}, dto={}", id, dto);

        Entity entity = getEntity(id);
        mapper.updateEntity(dto, entity);
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(entity);
    }

    @Transactional
    public void deleteById(@NonNull final ID id) {
        log.debug("deleteById id={}", id);

        final Entity entity = getEntity(id);
        if (isNull(entity.getDeletedAt())) {
            entity.setDeletedAt(LocalDateTime.now());
        }
    }

    @Transactional
    public List<Dto> getAll() {
        return mapper.toDtos(repository.findAll());
    }

    @Transactional
    public Dto getById(@NonNull final ID id) {
        final Entity entity = getEntity(id);
        return mapper.toDto(entity);
    }

    private Entity getEntity(final ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    }

}

