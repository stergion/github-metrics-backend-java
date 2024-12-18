package com.stergion.githubbackend.external.githubservice.client.models.error;

public record InternalServerError(int statusCode, String message) implements ErrorResponse {
}
