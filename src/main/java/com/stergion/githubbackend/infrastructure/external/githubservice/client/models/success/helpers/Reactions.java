package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Represents reaction counts for GitHub entities like comments, issues, and pull requests.
 */
public record Reactions(
        @PositiveOrZero(message = "Total count must be non-negative")
        int totalCount
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
            return String.format("Reactions[totalCount=%d]", totalCount);
        }
    }
}