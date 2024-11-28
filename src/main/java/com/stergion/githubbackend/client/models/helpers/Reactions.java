package com.stergion.githubbackend.client.models.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Represents reaction counts for GitHub entities like comments, issues, and pull requests.
 */
public record Reactions(
        @PositiveOrZero(message = "Total count must be non-negative")
        int totalCount
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
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