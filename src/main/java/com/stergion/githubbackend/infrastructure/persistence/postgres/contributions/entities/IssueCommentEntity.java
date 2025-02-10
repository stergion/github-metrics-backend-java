package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.AssociatedIssue;
import io.quarkus.logging.Log;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@Entity
@Table(name = "IssueComments")
public class IssueCommentEntity extends ContributionEntity {
    @NotNull
    @PastOrPresent
    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastEditedAt;
    private AssociatedIssue associatedIssue;
    private String body;

    private static final ObjectMapper MAPPER = new ObjectMapper();

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(LocalDateTime lastEditedAt) {
        this.lastEditedAt = lastEditedAt;
    }

    public AssociatedIssue getAssociatedIssue() {
        return associatedIssue;
    }

    public void setAssociatedIssue(
            AssociatedIssue associatedIssue) {
        this.associatedIssue = associatedIssue;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
