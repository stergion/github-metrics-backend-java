package com.stergion.githubbackend.infrastructure.persistence.users;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {
    public void delete(String login) {
        delete("login", login);
    }

    public User findByLogin(String login) {
        return find("login", login).firstResult();
    }

}