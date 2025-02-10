package com.stergion.githubbackend.infrastructure.persistence.mongo.users;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.common.PanacheUpdate;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<UserEntity> {

    private void setTimestamps(UserEntity user) {
        if (user.id == null) {  // New user
            user.createdAt = LocalDateTime.now();
            user.updatedAt = user.createdAt;
        } else {  // Existing user
            UserEntity originalUser = findById(user.id);
            if (originalUser == null) {
                throw new RuntimeException("Could not find original user.");
            }
            user.createdAt = originalUser.createdAt;
            user.updatedAt = LocalDateTime.now();
        }
    }

    private void processUsersForPersistOrUpdate(Iterable<UserEntity> users) {
        users.forEach(this::setTimestamps);
    }

    @Override
    public void persist(UserEntity user) {
        setTimestamps(user);
        PanacheMongoRepository.super.persist(user);
    }

    @Override
    public void persist(Stream<UserEntity> users) {
        users = users.peek(this::setTimestamps);
        PanacheMongoRepository.super.persist(users);
    }

    @Override
    public void persist(Iterable<UserEntity> users) {
        processUsersForPersistOrUpdate(users);
        PanacheMongoRepository.super.persist(users);
    }

    @Override
    public void persistOrUpdate(UserEntity user) {
        setTimestamps(user);
        PanacheMongoRepository.super.persistOrUpdate(user);
    }

    @Override
    public void persistOrUpdate(Iterable<UserEntity> users) {
        processUsersForPersistOrUpdate(users);
        PanacheMongoRepository.super.persistOrUpdate(users);
    }

    @Override
    public void persistOrUpdate(Stream<UserEntity> users) {
        users = users.peek(this::setTimestamps);
        PanacheMongoRepository.super.persistOrUpdate(users);
    }

    @Override
    public void persistOrUpdate(UserEntity firstEntity, UserEntity... entities) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public void update(UserEntity user) {
        setTimestamps(user);
        PanacheMongoRepository.super.update(user);
    }

    @Override
    public void update(Iterable<UserEntity> users) {
        processUsersForPersistOrUpdate(users);
        PanacheMongoRepository.super.update(users);
    }

    @Override
    public void update(Stream<UserEntity> users) {
        users = users.peek(this::setTimestamps);
        PanacheMongoRepository.super.update(users);
    }

    @Override
    public void update(UserEntity firstEntity, UserEntity... entities) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public PanacheUpdate update(String query, Object... params) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public PanacheUpdate update(String query, Map<String, Object> params) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public PanacheUpdate update(String query, Parameters params) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    public UserEntity findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public List<UserEntity> findByEmail(String email) {
        return list("email", email);
    }

    public UserEntity findByGitHubId(String githubId) {
        return find("github.id", githubId).firstResult();
    }

    public void deleteByLogin(String login) {
        delete("login", login);
    }
}