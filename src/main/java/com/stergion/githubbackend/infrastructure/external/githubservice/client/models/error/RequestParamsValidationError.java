package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.error;

import java.util.List;

public record RequestParamsValidationError(
        int statusCode,
        String message,
        List<ValidationInfo> details
) implements ErrorResponse {}
