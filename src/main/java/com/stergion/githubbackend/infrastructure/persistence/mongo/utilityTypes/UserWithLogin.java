package com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes;

public class UserWithLogin {
    public String login;

    public UserWithLogin() {
    }

    public UserWithLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "{\"user\": %s}".formatted(login);
    }
}
