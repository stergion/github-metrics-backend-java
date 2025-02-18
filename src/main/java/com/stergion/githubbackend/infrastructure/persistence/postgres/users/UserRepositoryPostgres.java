package com.stergion.githubbackend.infrastructure.persistence.postgres.users;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepositoryPostgres implements PanacheRepositoryBase<UserEntity, UUID> {

    public Uni<UserEntity> findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public Uni<List<UserEntity>> findByEmail(String email) {
        return list("email", email);
    }

    public Uni<UserEntity> findByGitHubId(String githubId) {
        return find("githubId", githubId).firstResult();
    }

    public Uni<Long> deleteByLogin(String login) {
        return delete("login", login);
    }
}
