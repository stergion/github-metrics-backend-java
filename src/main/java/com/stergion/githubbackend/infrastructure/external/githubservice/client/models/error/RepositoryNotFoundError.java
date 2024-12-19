package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.error;

public record RepositoryNotFoundError(int statusCode, String message) implements ErrorResponse {}
