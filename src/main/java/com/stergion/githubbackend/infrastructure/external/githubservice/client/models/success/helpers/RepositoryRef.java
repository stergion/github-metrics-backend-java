package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RepositoryRef(
        @NotBlank(message = "Repository name cannot be blank")
        String name,

        @NotNull(message = "Repository owner cannot be null")
        RepositoryOwner owner
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
            return String.format("RepositoryReference[name=%s, owner=%s]", name, owner.login());
        }
    }
}