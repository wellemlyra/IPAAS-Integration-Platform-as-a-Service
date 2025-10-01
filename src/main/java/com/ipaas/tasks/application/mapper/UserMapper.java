package com.ipaas.tasks.application.mapper;

import com.ipaas.tasks.application.dto.UserRequest;
import com.ipaas.tasks.application.dto.UserResponse;
import com.ipaas.tasks.domain.model.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserMapper {

    public UserEntity toEntity(UserRequest req) {
        if (req == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setName(req.getName());
        entity.setEmail(req.getEmail());
        // entity.setPassword() - você precisará lidar com a senha
        return entity;
    }

    public UserResponse toResponse(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        return response;
    }

    public List<UserResponse> toResponseList(List<UserEntity> list) {
        if (list == null) {
            return null;
        }

        return list.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}