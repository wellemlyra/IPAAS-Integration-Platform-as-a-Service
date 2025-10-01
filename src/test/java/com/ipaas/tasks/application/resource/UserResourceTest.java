package com.ipaas.tasks.application.resource;

import com.ipaas.tasks.application.dto.UserRequest;
import com.ipaas.tasks.application.dto.UserResponse;
import com.ipaas.tasks.domain.service.UserService;
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
class UserResourceTest {

    @InjectMock
    UserService service;

    @Test
    void list_shouldReturn200() {
        when(service.list()).thenReturn(List.of(new UserResponse()));
        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    void create_shouldReturn201() {
        UserRequest req = UserRequest.builder()
                .name("João da Silva")
                .email("joao@teste.com")
                .build();

        when(service.create(any(UserRequest.class))).thenReturn(new UserResponse());

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201);
    }

    @Test
    void update_shouldReturn200() {
        UserRequest req = UserRequest.builder()
                .name("Maria Oliveira")
                .email("maria@teste.com")
                .build();

        when(service.update(any(UUID.class), any(UserRequest.class))).thenReturn(new UserResponse());

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .put("/api/users/{id}", UUID.randomUUID())
                .then()
                .statusCode(200);
    }
}
