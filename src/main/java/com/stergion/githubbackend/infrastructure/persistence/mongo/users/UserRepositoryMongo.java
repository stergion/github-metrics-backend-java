package com.stergion.githubbackend.infrastructure.persistence.mongo.users;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.common.reactive.ReactivePanacheUpdate;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class UserRepositoryMongo implements ReactivePanacheMongoRepository<UserEntity> {

    private Uni<UserEntity> setTimestamps(UserEntity user) {
        if (user.id == null) {  // New user
            user.createdAt = LocalDateTime.now();
            user.updatedAt = user.createdAt;
            return Uni.createFrom().item(user);
        } else {  // Existing user
            return findById(user.id)
                    .onItem()
                    .ifNotNull()
                    .transform(originalUser -> {
                        user.createdAt = originalUser.createdAt;
                        user.updatedAt = LocalDateTime.now();
                        return user;
                    })
                    .onItem()
                    .ifNull()
                    .failWith(new RuntimeException("Could not find original user."));
        }
    }

    private void processUsersForPersistOrUpdate(Iterable<UserEntity> users) {
        users.forEach(this::setTimestamps);
    }

    @Override
    public Uni<UserEntity> persist(UserEntity user) {
        return setTimestamps(user).chain(ReactivePanacheMongoRepository.super::persist);
    }

    @Override
    public Uni<Void> persist(Stream<UserEntity> users) {
        var userList = users.toList();
        return this.persist(userList);
    }

    @Override
    public Uni<Void> persist(Iterable<UserEntity> users) {
        return Multi.createFrom()
                    .iterable(users)
                    .onItem()
                    .transformToUniAndConcatenate(this::setTimestamps)
                    .collect().asList()
                    .chain(ReactivePanacheMongoRepository.super::persist);
    }

    @Override
    public Uni<UserEntity> persistOrUpdate(UserEntity user) {
        return setTimestamps(user).chain(ReactivePanacheMongoRepository.super::persistOrUpdate);
    }

    @Override
    public Uni<Void> persistOrUpdate(Iterable<UserEntity> users) {
        return Multi.createFrom()
                    .iterable(users)
                    .onItem()
                    .transformToUniAndConcatenate(this::setTimestamps)
                    .collect().asList()
                    .chain(ReactivePanacheMongoRepository.super::persistOrUpdate);
    }

    @Override
    public Uni<Void> persistOrUpdate(Stream<UserEntity> users) {
        var userList = users.toList();
        return this.persistOrUpdate(userList);
    }

    @Override
    public Uni<Void> persistOrUpdate(UserEntity firstEntity, UserEntity... entities) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public Uni<UserEntity> update(UserEntity user) {
        return setTimestamps(user).chain(ReactivePanacheMongoRepository.super::update);
    }

    @Override
    public Uni<Void> update(Iterable<UserEntity> users) {
        return Multi.createFrom()
                    .iterable(users)
                    .onItem()
                    .transformToUniAndConcatenate(this::setTimestamps)
                    .collect().asList()
                .chain(ReactivePanacheMongoRepository.super::update);
    }

    @Override
    public Uni<Void> update(Stream<UserEntity> users) {
        var userList = users.toList();
        return this.update(userList);
    }

    @Override
    public Uni<Void> update(UserEntity firstEntity, UserEntity... entities) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public ReactivePanacheUpdate update(String query, Object... params) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public ReactivePanacheUpdate update(String query, Map<String, Object> params) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public ReactivePanacheUpdate update(String query, Parameters params) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    public Uni<UserEntity> findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public Uni<List<UserEntity>> findByEmail(String email) {
        return list("email", email);
    }

    public Uni<UserEntity> findByGitHubId(String githubId) {
        return find("github.id", githubId).firstResult();
    }

    public Uni<Long> deleteByLogin(String login) {
        return delete("login", login);
    }
}