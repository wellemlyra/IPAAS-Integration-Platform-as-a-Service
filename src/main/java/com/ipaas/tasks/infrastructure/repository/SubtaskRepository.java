package com.ipaas.tasks.infrastructure.repository;

import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.model.SubtaskEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SubtaskRepository implements PanacheRepositoryBase<SubtaskEntity, UUID> {

    /**
     * Conta subtarefas abertas (status diferente de CONCLUIDA) de uma determinada Task.
     */
    public long countOpenByTask(UUID taskId) {
        return count("task.id = ?1 and status <> ?2", taskId, Status.CONCLUIDA);
    }

    /**
     * Lista todas as subtarefas associadas a uma Task.
     */
    public List<SubtaskEntity> listByTask(UUID taskId) {
        return list("task.id", taskId);
    }

    /**
     * Verifica se existe ao menos uma subtask aberta para a Task.
     */
    public boolean existsOpenByTask(UUID taskId) {
        return countOpenByTask(taskId) > 0;
    }
}
