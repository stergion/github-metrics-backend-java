package com.stergion.githubbackend.infrastructure.persistence.utilityTypes;

public class UserWithLogin {
    public String login;

    public UserWithLogin(String login) {
        this.login = login;
    }
    @Override
    public String toString() {
        return "{\"user\": %s}".formatted(login);
    }
}
