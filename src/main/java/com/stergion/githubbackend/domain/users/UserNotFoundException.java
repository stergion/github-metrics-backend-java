package com.stergion.githubbackend.domain.users;

public class UserNotFoundException extends RuntimeException {
    String login;

    public UserNotFoundException(String login) {
        super("User not found: " + login);
        this.login = login;
    }
}
