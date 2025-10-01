package com.ipaas.tasks.infrastructure.repository;

import com.ipaas.tasks.domain.model.TaskEntity;
import com.ipaas.tasks.domain.model.Status;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<TaskEntity, UUID> {

    /**
     * Lista todas as tasks por status.
     */
    public List<TaskEntity> findByStatus(Status status) {
        return list("status", status);
    }

    /**
     * Lista todas as tasks de um usuário responsável.
     */
    public List<TaskEntity> findByAssignee(UUID assigneeId) {
        return list("assignee.id", assigneeId);
    }

    /**
     * Verifica se uma task existe pelo título.
     */
    public boolean existsByTitle(String title) {
        return find("title", title).firstResultOptional().isPresent();
    }
}
