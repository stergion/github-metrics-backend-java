package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents a label that can be applied to various GitHub entities like Issues and Pull Requests.
 */
public record LabelNode(
        @NotBlank(message = "Label name cannot be blank")
        String name,

        String description
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
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