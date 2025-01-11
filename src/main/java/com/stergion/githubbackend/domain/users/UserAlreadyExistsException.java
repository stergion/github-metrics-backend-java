package com.stergion.githubbackend.domain.users;

public class UserAlreadyExistsException extends RuntimeException {
    private final String username;

    public UserAlreadyExistsException(String username) {
        this(username, "User " + username + " already exists");
    }

    public UserAlreadyExistsException(String username, String message) {
        super(message);
        this.username = username;
    }
}
