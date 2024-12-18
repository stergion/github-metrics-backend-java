package com.stergion.githubbackend.external.githubservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.external.githubservice.client.exceptions.*;
import com.stergion.githubbackend.external.githubservice.client.models.error.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class ErrorResponseMapper {
    private final ObjectMapper objectMapper;

    public ErrorResponseMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private ErrorResponse toError(String errorResponse) {
        try {
            return objectMapper.readValue(errorResponse, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ServiceResponseException toServiceResponseException(ErrorResponse error) {
        return switch (error) {
            case RequestParamsValidationError e -> new RequestParamsValidationException(
                    e.message(),
                    e.details()
                     .stream()
                     .map(d -> new RequestParamsValidationException.ValidationError(
                             d.value(),
                             d.message(),
                             d.location(),
                             d.path())
                         )
                     .toList());
            case NotGithubUserError e -> new NotGithubUserException(e.message());
            case RepositoryNotFoundError e -> new RepositoryNotFoundException(e.message());
            case InternalServerError e -> new InternalServerException(e.message());
        };
    }

    public ServiceResponseException toThrowable(String errorResponse) {

        ErrorResponse error = toError(errorResponse);

        return toServiceResponseException(error);
    }


}
