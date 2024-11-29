package com.stergion.githubbackend.client.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.stergion.githubbackend.client.models.helpers.GitHubNodeRef;
import com.stergion.githubbackend.client.models.helpers.Reactions;
import com.stergion.githubbackend.client.models.helpers.RepositoryRef;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.Instant;

/**
 * Represents a comment on a GitHub commit
 */
public record CommitComment(
        @NotBlank(message = "Comment ID cannot be blank")
        String id,

        @NotNull(message = "Comment URL cannot be null")
        URI url,

        @NotNull(message = "Created date cannot be null")
        Instant createdAt,

        @NotNull(message = "Updated date cannot be null")
        Instant updatedAt,

        Instant publishedAt,

        Instant lastEditedAt,

        Integer position,

        @NotBlank(message = "Body cannot be blank")
        String body,

        GitHubNodeRef commit,

        @NotNull(message = "Repository cannot be null")
        RepositoryRef repository,

        @NotNull(message = "Reactions cannot be null")
        Reactions reactions
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("CommitComment[id=%s]", id);
        }
    }
}