package com.ipaas.tasks.application.resource;

import com.ipaas.tasks.application.dto.TaskRequest;
import com.ipaas.tasks.application.dto.TaskResponse;
import com.ipaas.tasks.application.dto.TaskStatusUpdateRequest;
import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.service.TaskService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import com.ipaas.tasks.domain.model.Priority;
import java.util.List;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class TaskResourceTest {

    @InjectMock
    TaskService service;

    @Test
    void list_shouldReturn200() {
        when(service.list()).thenReturn(List.of(new TaskResponse(), new TaskResponse()));

        given()
                .when()
                .get("/api/tasks")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    void create_shouldReturn201() {
        TaskRequest req = TaskRequest.builder()
                .title("Nova tarefa")
                .description("Descrição da tarefa")
                .status(Status.NOVA)
                .priority(Priority.MEDIA) // ✅ campo obrigatório preenchido
                .build();

        when(service.create(any(TaskRequest.class))).thenReturn(new TaskResponse());

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/tasks")
                .then()
                .statusCode(201);
    }

    @Test
    void updateStatus_shouldReturn200() {
        TaskStatusUpdateRequest req = TaskStatusUpdateRequest.builder()
                .status(Status.EM_ANDAMENTO)
                .build();

        when(service.updateStatus(any(UUID.class), any(TaskStatusUpdateRequest.class))).thenReturn(new TaskResponse());

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .patch("/api/tasks/{id}/status", UUID.randomUUID())
                .then()
                .statusCode(200);
    }
}
