package com.stergion.githubbackend.client.models.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URI;

public record GitHubNodeRef(
        @NotBlank(message = "Issue ID cannot be blank")
        String id,

        @NotNull(message = "Issue URL cannot be null")
        URI url
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("GitHubNodeRef[id=%s, url=%s]", id(), url());
        }
    }
}