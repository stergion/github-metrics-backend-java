package com.stergion.githubbackend.domain.users;

import io.smallrye.mutiny.Uni;

import java.util.List;

public interface UserRepository {
    Uni<User> persist(User user);

    Uni<Void> persist(List<User> users);

    Uni<User> update(User user);

    Uni<Void> update(List<User> users);

    Uni<Void> delete(User user);

    Uni<Void> deleteByLogin(String login);

    Uni<User> findByLogin(String login);

    Uni<List<User>> findByEmail(String email);

    Uni<User> findByGitHubId(String id);
}
