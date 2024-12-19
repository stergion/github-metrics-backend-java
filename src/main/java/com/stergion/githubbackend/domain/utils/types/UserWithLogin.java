package com.stergion.githubbackend.domain.utils.types;

public class UserWithLogin {
    String login;

    @Override
    public String toString() {
        return "{\"login\": %s}".formatted(login);
    }
}
