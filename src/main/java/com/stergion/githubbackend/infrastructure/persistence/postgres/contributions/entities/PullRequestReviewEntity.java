package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.PullRequestRef;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.PullRequestReviewComment;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestReviewState;
import io.quarkus.logging.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PullRequestReviews")
public class PullRequestReviewEntity extends ContributionEntity {

    @NotNull
    private PullRequestRef pullRequest;

    @NotNull
    @PastOrPresent
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private LocalDateTime lastEditedAt;

    @Enumerated(EnumType.STRING)
    private PullRequestReviewState state;

    private String body;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PullRequestReviewComment> comments = new ArrayList<>();

    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            Log.error(e.getClass() + ": " + e.getMessage() + ". \nCause: " + e.getCause());
            return "{id: %s, userLogin: %s, owner:%s, name:%s}".formatted(getId(),
                    getUser().getLogin(), getRepository().getOwner(), getRepository().getName());
        }
    }

    /*
     *********************
     * Getters / Setters *
     *********************
     */

    public PullRequestRef getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(
            PullRequestRef pullRequest) {
        this.pullRequest = pullRequest;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(LocalDateTime lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }

    public PullRequestReviewState getState() {
        return state;
    }

    public void setState(PullRequestReviewState state) {
        this.state = state;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<PullRequestReviewComment> getComments() {
        return List.copyOf(comments);
    }

    public void setComments(List<PullRequestReviewComment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
    }
}
