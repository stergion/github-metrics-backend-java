package com.stergion.githubbackend.external.githubservice.client.exceptions;

public final class InternalServerException extends ServiceResponseException {
    public InternalServerException(String message) {
        super(message, 500);
    }
}
