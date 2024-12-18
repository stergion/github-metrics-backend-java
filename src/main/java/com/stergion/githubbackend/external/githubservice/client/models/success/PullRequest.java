package com.stergion.githubbackend.external.githubservice.client.models.success;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.GitHubNodeRef;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.LabelsConnection;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.Reactions;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.RepositoryRef;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Represents a GitHub pull request
 */
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public record PullRequest(
        @NotBlank(message = "Pull request ID cannot be blank")
        String id,

        @NotNull(message = "Pull request URL cannot be null")
        URI url,

        @NotNull(message = "Created date cannot be null")
        Instant createdAt,

        @NotNull(message = "Updated date cannot be null")
        Instant updatedAt,

        Instant mergedAt,

        Instant closedAt,

        @NotNull(message = "State cannot be null")
        State state,

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Body cannot be blank")
        String body,

        @NotNull(message = "Repository cannot be null")
        RepositoryRef repository,

        @NotNull(message = "Reactions cannot be null")
        Reactions reactions,

        LabelsConnection labels,

        @NotNull(message = "Commits cannot be null")
        CommitsConnection commits,

        @NotNull(message = "Comments cannot be null")
        CommentsConnection comments,

        ClosingIssuesReferences closingIssuesReferences
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    public PullRequest {
        if (labels != null && labels.nodes() == null) {
            labels = new LabelsConnection(labels.totalCount(), List.of());
        }
        if (commits.nodes() == null) {
            commits = new CommitsConnection(commits.totalCount(), List.of());
        }
        if (closingIssuesReferences != null && closingIssuesReferences.nodes() == null) {
            closingIssuesReferences = new ClosingIssuesReferences(
                    closingIssuesReferences.totalCount(),
                    List.of()
            );
        }
    }

    public boolean hasLabels() {
        return labels != null && !labels.nodes().isEmpty();
    }

    public boolean hasCommits() {
        return !commits.nodes().isEmpty();
    }

    public boolean hasClosingIssues() {
        return closingIssuesReferences != null && !closingIssuesReferences.nodes().isEmpty();
    }

    public boolean isMerged() {
        return state == State.MERGED;
    }

    public boolean isClosed() {
        return state == State.CLOSED;
    }

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("PullRequest[id=%s, title=%s]", id, title);
        }
    }

    public enum State {
        OPEN, CLOSED, MERGED
    }

    public record CommitNode(PullRequestCommit commit) {
        public record PullRequestCommit(
                @NotBlank(message = "Commit ID cannot be blank")
                String id,

                @NotNull(message = "Commit URL cannot be null")
                URI commitUrl,

                @PositiveOrZero(message = "Changed files count must be non-negative")
                int changedFiles,

                @PositiveOrZero(message = "Additions count must be non-negative")
                int additions,

                @PositiveOrZero(message = "Deletions count must be non-negative")
                int deletions
        ) {
        }
    }

    public record CommitsConnection(
            @NotNull(message = "Total count cannot be null")
            int totalCount,
            List<CommitNode> nodes
    ) {
    }

    public record CommentsConnection(
            @NotNull(message = "Total count cannot be null")
            int totalCount
    ) {
    }

    public record ClosingIssuesReferences(
            @NotNull(message = "Total count cannot be null")
            int totalCount,
            List<GitHubNodeRef> nodes
    ) {
    }
}