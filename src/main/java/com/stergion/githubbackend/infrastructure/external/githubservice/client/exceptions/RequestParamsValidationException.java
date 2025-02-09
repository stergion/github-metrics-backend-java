package com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions;

import java.util.List;

public final class RequestParamsValidationException extends ServiceResponseException {
    private final List<ValidationError> validationErrors;

    public RequestParamsValidationException(String message,
                                            List<ValidationError> validationErrors) {
        super(message, 400);
        this.validationErrors = List.copyOf(validationErrors);
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public record ValidationError(
            String value,
            String message,
            String location,
            String path
    ) {}
}
