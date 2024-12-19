package com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions;

public final class NotGithubUserException extends ResourceNotFoundException {
    public NotGithubUserException(String message) {
        super(message);
    }
}
