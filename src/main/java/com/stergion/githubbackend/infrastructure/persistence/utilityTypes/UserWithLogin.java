package com.stergion.githubbackend.infrastructure.persistence.utilityTypes;

public class UserWithLogin {
    public String login;

    @Override
    public String toString() {
        return "{\"user\": %s}".formatted(login);
    }
}
