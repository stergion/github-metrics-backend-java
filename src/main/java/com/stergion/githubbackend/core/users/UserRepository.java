package com.stergion.githubbackend.core.users;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public User findByLogin(String login) {
        return find("login", login).firstResult();
    }

}