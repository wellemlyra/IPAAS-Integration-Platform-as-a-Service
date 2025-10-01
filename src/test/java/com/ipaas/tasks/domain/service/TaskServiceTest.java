package com.ipaas.tasks.domain.service;

import com.ipaas.tasks.application.dto.TaskRequest;
import com.ipaas.tasks.application.dto.TaskResponse;
import com.ipaas.tasks.application.dto.TaskStatusUpdateRequest;
import com.ipaas.tasks.application.mapper.TaskMapper;
import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.model.TaskEntity;
import com.ipaas.tasks.infrastructure.repository.SubtaskRepository;
import com.ipaas.tasks.infrastructure.repository.TaskRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class TaskServiceTest {

    @Inject
    TaskService taskService;

    @InjectMock
    TaskRepository taskRepository;

    @InjectMock
    TaskMapper taskMapper;

    @InjectMock
    SubtaskRepository subtaskRepository;

    @Test
    public void updateStatus_shouldThrow_whenOpenSubtasksExist() {
        // Arrange
        UUID taskId = UUID.randomUUID();

        // Mock da task existente - use qualquer status que não seja CONCLUIDA
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setStatus(Status.EM_ANDAMENTO); // ou outro status disponível

        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest();
        request.setStatus(Status.CONCLUIDA);

        when(taskRepository.findById(taskId)).thenReturn(existingTask);

        // Mock para verificar subtasks abertas
        when(subtaskRepository.existsOpenByTask(taskId)).thenReturn(true);

        // Act & Assert
        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            taskService.updateStatus(taskId, request);
        });

        assertEquals(400, exception.getResponse().getStatus());
        assertTrue(exception.getMessage().contains("subtasks") ||
                exception.getMessage().contains("andamento"));
    }

    @Test
    public void updateStatus_shouldSucceed_whenNoOpenSubtasks() {
        // Arrange
        UUID taskId = UUID.randomUUID();

        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setStatus(Status.EM_ANDAMENTO); // use status disponível

        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest();
        request.setStatus(Status.CONCLUIDA);

        TaskResponse expectedResponse = new TaskResponse();
        expectedResponse.setId(taskId);
        expectedResponse.setStatus(Status.CONCLUIDA);

        when(taskRepository.findById(taskId)).thenReturn(existingTask);
        when(subtaskRepository.existsOpenByTask(taskId)).thenReturn(false);
        when(taskMapper.toResponse(existingTask)).thenReturn(expectedResponse);

        // Act
        TaskResponse result = taskService.updateStatus(taskId, request);

        // Assert
        assertNotNull(result);
        assertEquals(Status.CONCLUIDA, result.getStatus());
        assertEquals(Status.CONCLUIDA, existingTask.getStatus());
    }

    @Test
    public void get_shouldThrow_whenTaskNotFound() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(null);

        // Act & Assert
        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            taskService.get(taskId);
        });

        assertEquals(404, exception.getResponse().getStatus());
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    public void list_shouldReturnTaskList() {
        // Arrange
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(UUID.randomUUID());
        taskEntity.setTitle("Test Task");
        taskEntity.setStatus(Status.EM_ANDAMENTO); // use status disponível

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(taskEntity.getId());
        taskResponse.setTitle("Test Task");
        taskResponse.setStatus(Status.EM_ANDAMENTO); // use o mesmo status

        when(taskRepository.listAll()).thenReturn(Arrays.asList(taskEntity));
        when(taskMapper.toResponseList(any())).thenReturn(Arrays.asList(taskResponse));

        // Act
        List<TaskResponse> result = taskService.list();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
    }
}