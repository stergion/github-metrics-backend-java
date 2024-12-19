package com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions;

public sealed class ResourceNotFoundException extends ServiceResponseException
        permits NotGithubUserException, RepositoryNotFoundException {

    protected ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
