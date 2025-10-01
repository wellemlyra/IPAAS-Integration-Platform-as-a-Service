package com.ipaas.tasks.infrastructure.repository;

import com.ipaas.tasks.domain.model.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID> {

    public boolean emailExists(String email) {
        return find("email", email).firstResultOptional().isPresent();
    }
}
