package com.ipaas.tasks.domain.service;

import com.ipaas.tasks.application.dto.*;
import com.ipaas.tasks.application.exception.BusinessException;
import com.ipaas.tasks.application.mapper.UserMapper;
import com.ipaas.tasks.domain.model.UserEntity;
import com.ipaas.tasks.infrastructure.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.*;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository repository;
    @Inject
    UserMapper mapper;

    public java.util.List<UserResponse> list() {
        return mapper.toResponseList(repository.listAll());
    }

    public UserResponse get(java.util.UUID id) {
        UserEntity e = repository.findById(id);
        if (e == null) throw new BusinessException("Usuário não encontrado", Response.Status.NOT_FOUND);
        return mapper.toResponse(e);
    }

    @Transactional
    public UserResponse create(UserRequest req) {
        if (repository.emailExists(req.getEmail()))
            throw new BusinessException("E-mail já utilizado", Response.Status.CONFLICT);
        UserEntity e = mapper.toEntity(req);
        repository.persist(e);
        return mapper.toResponse(e);
    }

    @Transactional
    public UserResponse update(java.util.UUID id, UserRequest req) {
        UserEntity e = repository.findById(id);
        if (e == null) throw new BusinessException("Usuário não encontrado", Response.Status.NOT_FOUND);
        e.setName(req.getName());
        e.setEmail(req.getEmail());
        return mapper.toResponse(e);
    }

    @Transactional
    public void delete(java.util.UUID id) {
        if (!repository.deleteById(id))
            throw new BusinessException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
}
