package com.ipaas.tasks.domain.service;

import com.ipaas.tasks.application.dto.TaskRequest;
import com.ipaas.tasks.application.dto.TaskResponse;
import com.ipaas.tasks.application.dto.TaskStatusUpdateRequest;
import com.ipaas.tasks.application.mapper.TaskMapper;
import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.model.TaskEntity;
import com.ipaas.tasks.infrastructure.repository.SubtaskRepository;
import com.ipaas.tasks.infrastructure.repository.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository repository;

    @Inject
    TaskMapper mapper;

    @Inject
    SubtaskRepository subtaskRepository;

    public List<TaskResponse> list() {
        return mapper.toResponseList(repository.listAll());
    }

    public TaskResponse get(UUID id) {
        TaskEntity entity = repository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Task not found", 404);
        }
        return mapper.toResponse(entity);
    }

    @Transactional
    public TaskResponse create(TaskRequest req) {
        TaskEntity entity = mapper.toEntity(req);
        repository.persist(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public TaskResponse update(UUID id, TaskRequest req) {
        TaskEntity entity = repository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Task not found", 404);
        }
        entity.setTitle(req.getTitle());
        entity.setDescription(req.getDescription());
        entity.setStatus(req.getStatus());
        entity.setPriority(req.getPriority());
        entity.setDueDate(req.getDueDate());
        return mapper.toResponse(entity);
    }

    @Transactional
    public TaskResponse updateStatus(UUID id, TaskStatusUpdateRequest req) {
        TaskEntity entity = repository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Task not found", 404);
        }

        // Regra: só pode concluir se não tiver subtarefas abertas
        if (req.getStatus() == Status.CONCLUIDA) {
            boolean hasOpenSubtasks = subtaskRepository.existsOpenByTask(id);
            if (hasOpenSubtasks) {
                throw new WebApplicationException(
                        "Não é possível concluir a tarefa pois existem subtarefas em andamento.",
                        400
                );
            }
        }

        entity.setStatus(req.getStatus());
        return mapper.toResponse(entity);
    }

    @Transactional
    public void delete(UUID id) {
        TaskEntity entity = repository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Task not found", 404);
        }
        repository.delete(entity);
    }
    public List<TaskResponse> listByStatus(Status status) {
        return mapper.toResponseList(repository.findByStatus(status));
    }


}
