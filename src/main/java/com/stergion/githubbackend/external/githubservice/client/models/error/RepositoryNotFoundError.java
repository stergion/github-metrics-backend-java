package com.stergion.githubbackend.external.githubservice.client.models.error;

public record RepositoryNotFoundError(int statusCode, String message) implements ErrorResponse {}
