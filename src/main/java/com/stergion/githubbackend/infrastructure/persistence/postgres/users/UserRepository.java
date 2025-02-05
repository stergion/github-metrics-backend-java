package com.stergion.githubbackend.infrastructure.persistence.postgres.users;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, UUID> {

    public Uni<User> findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public Uni<List<User>> findByEmail(String email) {
        return list("email", email);
    }

    public Uni<User> findByGitHubId(String githubId) {
        return find("githubId", githubId).firstResult();
    }

    public Uni<Long> deleteByLogin(String login) {
        return delete("login", login);
    }
}
