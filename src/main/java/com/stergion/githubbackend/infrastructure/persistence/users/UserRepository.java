package com.stergion.githubbackend.infrastructure.persistence.users;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.common.PanacheUpdate;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {
    private void processUsersForPersistOrUpdate(Iterable<User> users) {
        users.forEach(this::setTimestamps);
    }

    @Override
    public void persist(User user) {
        setTimestamps(user);
        PanacheMongoRepository.super.persist(user);
    }

    @Override
    public void persist(Stream<User> users) {
        users = users.peek(this::setTimestamps);
        PanacheMongoRepository.super.persist(users);
    }

    @Override
    public void persist(Iterable<User> users) {
        processUsersForPersistOrUpdate(users);
        PanacheMongoRepository.super.persist(users);
    }

    @Override
    public void persistOrUpdate(User user) {
        setTimestamps(user);
        PanacheMongoRepository.super.persistOrUpdate(user);
    }

    @Override
    public void persistOrUpdate(Iterable<User> users) {
        processUsersForPersistOrUpdate(users);
        PanacheMongoRepository.super.persistOrUpdate(users);
    }

    @Override
    public void persistOrUpdate(Stream<User> users) {
        users = users.peek(this::setTimestamps);
        PanacheMongoRepository.super.persistOrUpdate(users);
    }

    @Override
    public void persistOrUpdate(User firstEntity, User... entities) {
        throw new UnsupportedOperationException(
                "Raw updates not supported to protect data integrity");
    }

    @Override
    public void update(User user) {
        setTimestamps(user);
        PanacheMongoRepository.super.update(user);
    }

    @Override
    public void update(Iterable<User> users) {
        processUsersForPersistOrUpdate(users);
        PanacheMongoRepository.super.update(users);
    }

    @Override
    public void update(Stream<User> users) {
        users = users.peek(this::setTimestamps);
        PanacheMongoRepository.super.update(users);
    }

    @Override
    public void update(User firstEntity, User... entities) {
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

    public User findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public List<User> findByEmail(String email) {
        return list("email", email);
    }

    public User findByGitHubId(String githubId) {
        return find("github.id", githubId).firstResult();
    }
}