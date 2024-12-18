package com.stergion.githubbackend.external.githubservice.client.models.error;

public record NotGithubUserError(int statusCode, String message) implements ErrorResponse {}
