package com.stergion.githubbackend.client.models.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents an author/user reference used across various GitHub entities.
 */
public record Author(
        @NotBlank(message = "Login cannot be blank")
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
            return String.format("Author[login=%s]", login);
        }
    }
}