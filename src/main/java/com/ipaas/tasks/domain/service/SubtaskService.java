package com.ipaas.tasks.domain.service;

import com.ipaas.tasks.application.dto.SubtaskRequest;
import com.ipaas.tasks.application.dto.SubtaskResponse;
import com.ipaas.tasks.application.dto.SubtaskStatusUpdateRequest;
import com.ipaas.tasks.application.exception.BusinessException;
import com.ipaas.tasks.application.mapper.SubtaskMapper;
import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.model.SubtaskEntity;
import com.ipaas.tasks.infrastructure.repository.SubtaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SubtaskService {

    @Inject
    SubtaskRepository repository;

    @Inject
    SubtaskMapper mapper;

    public List<SubtaskResponse> list() {
        return mapper.toResponseList(repository.listAll());
    }

    public SubtaskResponse get(UUID id) {
        SubtaskEntity entity = repository.findById(id);
        if (entity == null) {
            throw new BusinessException("Subtask não encontrada", Response.Status.NOT_FOUND);
        }
        return mapper.toResponse(entity);
    }

    public List<SubtaskResponse> listByTask(UUID taskId) {
        return mapper.toResponseList(repository.listByTask(taskId));
    }

    @Transactional
    public SubtaskResponse create(SubtaskRequest req) {
        SubtaskEntity entity = mapper.toEntity(req);
        repository.persist(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public SubtaskResponse update(UUID id, SubtaskRequest req) {
        SubtaskEntity entity = repository.findById(id);
        if (entity == null) {
            throw new BusinessException("Subtask não encontrada", Response.Status.NOT_FOUND);
        }
        entity.setTitle(req.getTitle());
        entity.setStatus(req.getStatus());
        return mapper.toResponse(entity);
    }

    @Transactional
    public SubtaskResponse updateStatus(UUID id, SubtaskStatusUpdateRequest req) {
        SubtaskEntity entity = repository.findById(id);
        if (entity == null) {
            throw new BusinessException("Subtask não encontrada", Response.Status.NOT_FOUND);
        }

        entity.setStatus(req.getStatus());

        if (req.getStatus() == Status.CONCLUIDA) {
            entity.setCompletedAt(OffsetDateTime.now());
        } else {
            entity.setCompletedAt(null);
        }

        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.deleteById(id)) {
            throw new BusinessException("Subtask não encontrada", Response.Status.NOT_FOUND);
        }
    }
}
