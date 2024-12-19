package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.error;

public record InternalServerError(int statusCode, String message) implements ErrorResponse {
}
