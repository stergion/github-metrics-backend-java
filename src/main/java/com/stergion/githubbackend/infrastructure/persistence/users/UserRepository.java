package com.stergion.githubbackend.infrastructure.persistence.users;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {
    @Override
    public void persist(User user) {
        setTimestamps(user);
        PanacheMongoRepository.super.persist(user);
    }

    @Override
    public void update(User user) {
        setTimestamps(user);
        PanacheMongoRepository.super.update(user);
    }
    @Override
    public void update(Iterable<User> users) {
        users.forEach(this::setTimestamps);

        PanacheMongoRepository.super.update(users);
    }

    @Override
    public void update(Stream<User> users) {
        Stream<User> users2 = users.peek((Consumer<? super User>) this::setTimestamps);

        PanacheMongoRepository.super.update(users2);
    }

    public void delete(String login) {
        delete("login", login);
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