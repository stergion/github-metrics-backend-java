package com.stergion.githubbackend.external.githubservice.client.models.success;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.Author;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.LabelsConnection;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.Reactions;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.RepositoryRef;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Represents a GitHub issue with its associated metadata.
 */
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public record Issue(
        @NotBlank(message = "Issue ID cannot be blank")
        String id,

        @NotNull(message = "Issue URL cannot be null")
        URI url,

        @NotNull(message = "Created date cannot be null")
        Instant createdAt,

        @NotNull(message = "Updated date cannot be null")
        Instant updatedAt,

        Instant closedAt,

        @NotNull(message = "State cannot be null")
        State state,

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Body cannot be blank")
        String body,

        @NotNull(message = "Repository cannot be null")
        RepositoryRef repository,

        TimelineItems timelineItems,

        @NotNull(message = "Reactions cannot be null")
        Reactions reactions,

        LabelsConnection labels,

        @NotNull(message = "Comments cannot be null")
        CommentsConnection comments
) {
    /**
     * Represents the state of an issue
     */
    public enum State {
        OPEN, CLOSED
    }

    /**
     * Represents a timeline item in an issue
     */
    public record TimelineItem(
            Author actor
    ) {}

    /**
     * Represents a collection of timeline items
     */
    public record TimelineItems(
            List<TimelineItem> nodes
    ) {}

    /**
     * Represents a collection of comments
     */
    public record CommentsConnection(
            @NotNull(message = "Total count cannot be null")
            int totalCount
    ) {}

    /**
     * Compact constructor for validation
     */
    public Issue {
        // Ensure collections are never null
        if (timelineItems != null && timelineItems.nodes() == null) {
            timelineItems = new TimelineItems(List.of());
        }
        if (labels != null && labels.nodes() == null) {
            labels = new LabelsConnection(labels.totalCount(), List.of());
        }
    }

    public boolean hasLabels() {
        return labels != null && !labels.nodes().isEmpty();
    }

    public boolean hasTimelineItems() {
        return timelineItems != null && !timelineItems.nodes().isEmpty();
    }

//    @JsonIgnore
    public boolean isClosed() {
        return state == State.CLOSED;
    }

    private static final ObjectWriter WRITER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("Issue[id=%s, title=%s]", id, title);
        }
    }
}