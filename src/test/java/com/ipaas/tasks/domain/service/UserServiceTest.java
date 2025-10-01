package com.ipaas.tasks.domain.service;

import com.ipaas.tasks.application.dto.UserRequest;
import com.ipaas.tasks.application.dto.UserResponse;
import com.ipaas.tasks.application.exception.BusinessException;
import com.ipaas.tasks.application.mapper.UserMapper;
import com.ipaas.tasks.domain.model.UserEntity;
import com.ipaas.tasks.infrastructure.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock; // PACOTE CORRETO para Quarkus 3.x!
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService userService;

    @InjectMock
    UserRepository repository;

    @InjectMock
    UserMapper mapper;

    @Test
    public void list_shouldReturnUserList() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setName("João Silva");
        userEntity.setEmail("joao@email.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setName("João Silva");
        userResponse.setEmail("joao@email.com");

        when(repository.listAll()).thenReturn(Arrays.asList(userEntity));
        when(mapper.toResponseList(any())).thenReturn(Arrays.asList(userResponse));

        // Act
        List<UserResponse> result = userService.list();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getName());
        assertEquals("joao@email.com", result.get(0).getEmail());
    }

    @Test
    public void get_shouldThrow_whenUserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(repository.findById(userId)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.get(userId);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        assertEquals(Response.Status.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void create_shouldThrow_whenEmailExists() {
        // Arrange
        UserRequest request = UserRequest.builder()
                .name("João Silva")
                .email("joao@email.com")
                .build();

        when(repository.emailExists("joao@email.com")).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.create(request);
        });

        assertEquals("E-mail já utilizado", exception.getMessage());
        assertEquals(Response.Status.CONFLICT, exception.getStatus());
    }

    @Test
    public void create_shouldSuccess_whenEmailNotExists() {
        // Arrange
        UserRequest request = UserRequest.builder()
                .name("Maria Santos")
                .email("maria@email.com")
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setName("Maria Santos");
        userEntity.setEmail("maria@email.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setName("Maria Santos");
        userResponse.setEmail("maria@email.com");

        when(repository.emailExists("maria@email.com")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(userEntity);
        when(mapper.toResponse(userEntity)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals("Maria Santos", result.getName());
        assertEquals("maria@email.com", result.getEmail());
        verify(repository, times(1)).persist(userEntity);
    }

    @Test
    public void delete_shouldThrow_whenUserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(repository.deleteById(userId)).thenReturn(false);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.delete(userId);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        assertEquals(Response.Status.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void delete_shouldSuccess_whenUserExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(repository.deleteById(userId)).thenReturn(true);

        // Act & Assert - Não deve lançar exceção
        assertDoesNotThrow(() -> {
            userService.delete(userId);
        });

        verify(repository, times(1)).deleteById(userId);
    }
}