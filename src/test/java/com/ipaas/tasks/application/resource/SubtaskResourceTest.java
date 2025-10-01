package com.ipaas.tasks.application.resource;

import com.ipaas.tasks.application.dto.SubtaskRequest;
import com.ipaas.tasks.application.dto.SubtaskResponse;
import com.ipaas.tasks.application.dto.SubtaskStatusUpdateRequest;
import com.ipaas.tasks.domain.model.Status;
import com.ipaas.tasks.domain.service.SubtaskService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class SubtaskResourceTest {

    @InjectMock
    SubtaskService service;

    @Test
    void list_shouldReturn200() {
        when(service.list()).thenReturn(List.of(new SubtaskResponse()));

        given()
                .when()
                .get("/api/subtasks")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    void create_shouldReturn201() {
        SubtaskRequest req = SubtaskRequest.builder()
                .title("Estudar Quarkus")
                .status(Status.NOVA)
                .taskId(UUID.randomUUID())
                .build();

        when(service.create(any(SubtaskRequest.class))).thenReturn(new SubtaskResponse());

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/subtasks")
                .then()
                .statusCode(201);
    }

    @Test
    void updateStatus_shouldReturn200() {
        SubtaskStatusUpdateRequest req = SubtaskStatusUpdateRequest.builder()
                .status(Status.EM_ANDAMENTO)
                .build();

        when(service.updateStatus(any(UUID.class), any(SubtaskStatusUpdateRequest.class))).thenReturn(new SubtaskResponse());

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .patch("/api/subtasks/{id}/status", UUID.randomUUID())
                .then()
                .statusCode(200);
    }
}
