package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.Author;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.GitHubNodeRef;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.RepositoryRef;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Represents a GitHub pull request review
 */
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public record PullRequestReview(
        @NotBlank(message = "Review ID cannot be blank")
        String id,

        @NotNull(message = "Review URL cannot be null")
        URI url,

        @NotNull(message = "Review URL cannot be null")
        Instant createdAt,

        @NotNull(message = "Review URL cannot be null")
        Instant updatedAt,

        Instant publishedAt,

        Instant submittedAt,

        Instant lastEditedAt,

        @NotNull(message = "State cannot be null")
        ReviewState state,

        @NotBlank(message = "Body cannot be blank")
        String body,

        @NotNull(message = "Repository cannot be null")
        RepositoryRef repository,

        @NotNull(message = "Pull request cannot be null")
        GitHubNodeRef pullRequest,

        @NotNull(message = "Comments cannot be null")
        CommentsConnection comments
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
            return String.format("PullRequestReview[id=%s, state=%s]", id, state);
        }
    }

    public enum ReviewState {
        PENDING, COMMENTED, APPROVED, CHANGES_REQUESTED, DISMISSED
    }

    public record ReviewComment(
            @NotBlank(message = "Comment ID cannot be blank")
            String id,

            @NotNull(message = "Comment URL cannot be null")
            URI url,

            @NotBlank(message = "Body cannot be blank")
            String body,

            Author author
    ) {
    }

    public record CommentsConnection(
            @NotNull(message = "Total count cannot be null")
            int totalCount,

            List<ReviewComment> nodes
    ) {
        public CommentsConnection {
            nodes = nodes != null ? List.copyOf(nodes) : List.of();
        }
    }
}