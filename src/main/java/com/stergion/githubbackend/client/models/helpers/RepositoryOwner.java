package com.stergion.githubbackend.client.models.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents the owner of a repository.
 * This is used across various GitHub entities to reference repository ownership.
 */
public record RepositoryOwner(
        @NotBlank(message = "Owner login cannot be blank")
        String login
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("RepositoryOwner[login=%s]", login);
        }
    }
}