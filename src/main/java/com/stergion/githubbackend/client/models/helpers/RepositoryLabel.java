package com.stergion.githubbackend.client.models.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents a label that can be applied to various GitHub entities like Issues and Pull Requests.
 */
public record RepositoryLabel(
        @NotBlank(message = "Label name cannot be blank")
        String name,

        String description
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("RepositoryLabel[name=%s, description=%s]",
                    name, description != null ? description : "null");
        }
    }
}