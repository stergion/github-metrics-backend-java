package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.error;

public record NotGithubUserError(int statusCode, String message) implements ErrorResponse {}
