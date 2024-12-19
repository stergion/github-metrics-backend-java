package com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions;

public final class InternalServerException extends ServiceResponseException {
    public InternalServerException(String message) {
        super(message, 500);
    }
}
