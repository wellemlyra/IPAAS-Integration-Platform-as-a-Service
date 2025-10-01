package com.ipaas.tasks.domain.service;

import com.ipaas.tasks.application.dto.SubtaskRequest;
import com.ipaas.tasks.application.dto.SubtaskResponse;
import com.ipaas.tasks.application.mapper.SubtaskMapper;
import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.model.SubtaskEntity;
import com.ipaas.tasks.domain.model.TaskEntity;
import com.ipaas.tasks.infrastructure.repository.SubtaskRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class SubtaskServiceTest {

    @Inject
    SubtaskService service;

    @InjectMock
    SubtaskRepository repository;

    @InjectMock
    SubtaskMapper mapper;

    @Test
    @DisplayName("Cria subtarefa e retorna payload mapeado")
    void create_shouldPersist_andReturnResponse() {
        UUID taskId = UUID.randomUUID();

        SubtaskRequest req = SubtaskRequest.builder()
                .title("Estudar Quarkus")
                .status(Status.NOVA)
                .taskId(taskId)
                .build();

        TaskEntity task = TaskEntity.builder().id(taskId).build();

        SubtaskEntity entity = SubtaskEntity.builder()
                .id(UUID.randomUUID())
                .title(req.getTitle())
                .status(req.getStatus())
                .task(task)
                .build();

        SubtaskResponse resp = SubtaskResponse.builder()
                .title(entity.getTitle())
                .status(entity.getStatus())
                .taskId(taskId)
                .build();

        when(mapper.toEntity(any(SubtaskRequest.class))).thenReturn(entity);
        when(mapper.toResponse(any(SubtaskEntity.class))).thenReturn(resp);

        SubtaskResponse result = service.create(req);

        verify(repository, times(1)).persist(entity);
        verify(mapper, times(1)).toResponse(entity);
        assert result != null;
        assert result.getTitle().equals("Estudar Quarkus");
    }
}
