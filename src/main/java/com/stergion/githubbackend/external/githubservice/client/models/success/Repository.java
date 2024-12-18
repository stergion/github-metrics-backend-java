package com.stergion.githubbackend.external.githubservice.client.models.success;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.LabelsConnection;
import com.stergion.githubbackend.external.githubservice.client.models.success.helpers.RepositoryOwner;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.net.URI;
import java.util.List;

/**
 * Represents a GitHub repository with all its associated metadata
 */
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public record Repository(
        @NotBlank(message = "Repository ID cannot be blank")
        String id,

        @NotBlank(message = "Repository name cannot be blank")
        String name,

        @NotNull(message = "Repository URL cannot be null")
        URI url,

        @NotNull(message = "Repository owner cannot be null")
        RepositoryOwner owner,

        @PositiveOrZero(message = "Stargazer count must be non-negative")
        int stargazerCount,

        @PositiveOrZero(message = "Fork count must be non-negative")
        int forkCount,

        LanguageInfo primaryLanguage,

        @NotNull(message = "Languages information cannot be null")
        LanguagesConnection languages,

        LabelsConnection labels,

        RepositoryTopicsConnection repositoryTopics,

        @NotNull(message = "Watchers information cannot be null")
        WatchersConnection watchers
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    /**
     * Compact constructor for validation
     */
    public Repository {
        // Ensure non-null collections
        if (languages.edges == null) {
            languages = new LanguagesConnection(
                    languages.totalCount(),
                    languages.totalSize(),
                    List.of()
            );
        }
        if (labels != null && labels.nodes() == null) {
            labels = new LabelsConnection(labels.totalCount(), List.of());
        }
        if (repositoryTopics != null && repositoryTopics.nodes == null) {
            repositoryTopics = new RepositoryTopicsConnection(
                    repositoryTopics.totalCount(),
                    List.of()
            );
        }
    }

    @JsonIgnore
    public boolean hasTopics() {
        return repositoryTopics != null && !repositoryTopics.nodes().isEmpty();
    }

    @JsonIgnore
    public boolean hasLabels() {
        return labels != null && !labels.nodes().isEmpty();
    }

    @JsonIgnore
    @JsonProperty("nameWithOwner")
    public String getNameWithOwner() {
        return owner.login() + "/" + name;
    }

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("Repository[id=%s, nameWithOwner=%s]", id, getNameWithOwner());
        }
    }

    /**
     * Represents a single programming language with its name
     */
    public record LanguageInfo(
            @NotBlank(message = "Language name cannot be blank")
            String name
    ) {
    }

    /**
     * Represents an edge in the languages connection with size information
     */
    public record LanguageEdge(
            @PositiveOrZero(message = "Language size must be non-negative")
            int size,

            @NotNull(message = "Language node cannot be null")
            LanguageInfo node
    ) {
    }

    /**
     * Represents the languages connection with total counts and edges
     */
    public record LanguagesConnection(
            @PositiveOrZero(message = "Total count must be non-negative")
            int totalCount,

            @PositiveOrZero(message = "Total size must be non-negative")
            int totalSize,

            @NotNull(message = "Language edges cannot be null")
            List<LanguageEdge> edges
    ) {
    }

    /**
     * Represents a repository topic node
     */
    public record TopicNode(
            @NotBlank(message = "Topic name cannot be blank")
            String name
    ) {
    }

    /**
     * Represents a repository topic wrapper
     */
    public record RepositoryTopic(
            @NotNull(message = "Topic cannot be null")
            TopicNode topic
    ) {
    }

    /**
     * Represents the repository topics connection
     */
    public record RepositoryTopicsConnection(
            @PositiveOrZero(message = "Total count must be non-negative")
            int totalCount,

            @NotNull(message = "Topic nodes cannot be null")
            List<RepositoryTopic> nodes
    ) {
    }

    /**
     * Represents the watchers connection
     */
    public record WatchersConnection(
            @PositiveOrZero(message = "Total count must be non-negative")
            int totalCount
    ) {
    }
}