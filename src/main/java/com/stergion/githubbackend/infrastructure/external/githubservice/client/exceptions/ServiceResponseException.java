package com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions;

public sealed class ServiceResponseException extends RuntimeException
        permits RequestParamsValidationException, ResourceNotFoundException,
        InternalServerException {

    private final int statusCode;

    protected ServiceResponseException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    protected ServiceResponseException(String message, int statusCode) {
        this(message, statusCode, null);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
