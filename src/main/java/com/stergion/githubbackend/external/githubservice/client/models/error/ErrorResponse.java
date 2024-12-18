package com.stergion.githubbackend.external.githubservice.client.models.error;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "name"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RequestParamsValidationError.class, name = "RequestParamsValidationError"),
        @JsonSubTypes.Type(value = NotGithubUserError.class, name = "NotGithubUser"),
        @JsonSubTypes.Type(value = InternalServerError.class, name = "InternalServerError"),
        @JsonSubTypes.Type(value = RepositoryNotFoundError.class, name = "RepositoryNotFound")
})
public sealed interface ErrorResponse
        permits RequestParamsValidationError, NotGithubUserError, RepositoryNotFoundError,
        InternalServerError {
    int statusCode();
    String message();
}
