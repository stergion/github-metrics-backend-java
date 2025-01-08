package com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.Author;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.CommitFile;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.Reactions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a GitHub commit with its associated metadata, comments, and files.
 */
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public record CommitGH(
        @NotBlank(message = "Commit ID cannot be blank")
        String id,

        @NotBlank(message = "Commit OID cannot be blank")
        String oid,

        @NotNull(message = "Commit URL cannot be null")
        URI commitUrl,

        @NotNull(message = "Committed date cannot be null")
        LocalDate committedDate,

        LocalDate pushedDate,

        @PositiveOrZero(message = "Changed files count must be non-negative")
        int changedFiles,

        @PositiveOrZero(message = "Additions count must be non-negative")
        int additions,

        @PositiveOrZero(message = "Deletions count must be non-negative")
        int deletions,

        @NotBlank(message = "Commit message cannot be blank")
        String message,

        @NotNull(message = "Comments cannot be null")
        CommentsConnection comments,

        AssociatedPullRequests associatedPullRequests,

        List<CommitFile> files
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    /**
     * Compact constructor for validation
     */
    public CommitGH {
        // Ensure lists are never null
        files = files != null ? List.copyOf(files) : List.of();

        if (comments.nodes() == null) {
            comments = new CommentsConnection(List.of());
        }

        if (associatedPullRequests != null && associatedPullRequests.nodes() == null) {
            associatedPullRequests = new AssociatedPullRequests(List.of());
        }
    }

    @JsonIgnore
    public boolean hasFiles() {
        return files!=null && !files.isEmpty();
    }

    @JsonIgnore
    public boolean hasComments() {
        return comments!=null && comments.nodes()!=null && !comments.nodes().isEmpty();
    }

    @JsonIgnore
    public boolean hasAssociatedPullRequests() {
        return associatedPullRequests != null && associatedPullRequests.nodes()!=null && !associatedPullRequests.nodes().isEmpty();
    }

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("Commit[id=%s, message=%s]", id, message);
        }
    }

    /**
     * Represents a comment on a commit
     */
    public record CommitComment(
            LocalDate publishedAt,
            int position,
            @NotBlank(message = "Comment body cannot be blank")
            String body,
            Author author,
            @NotNull(message = "Reactions cannot be null")
            Reactions reactions
    ) {}

    /**
     * Represents a connection to commit comments
     */
    public record CommentsConnection(
            @NotNull(message = "Comment nodes cannot be null")
            List<CommitComment> nodes
    ) {}

    /**
     * Represents a reference to a pull request
     */
    public record PullRequestRef(
            @NotBlank(message = "Pull request ID cannot be blank")
            String id,

            @NotNull(message = "Pull request URL cannot be null")
            URI url
    ) {}

    /**
     * Represents associated pull requests for the commit
     */
    public record AssociatedPullRequests(
            @NotNull(message = "Pull request nodes cannot be null")
            List<PullRequestRef> nodes
    ) {}
}