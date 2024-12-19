package com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions;

public final class RepositoryNotFoundException extends ResourceNotFoundException {

    public RepositoryNotFoundException(String message) {
        super(message);
    }
}
