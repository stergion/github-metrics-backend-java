package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.AssociatedIssue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
@Table(name = "IssueComments")
public class IssueComment extends Contribution {
    @NotNull
    @PastOrPresent
    private LocalDate createdAt;

    private LocalDate publishedAt;
    private LocalDate updatedAt;
    private LocalDate lastEditedAt;
    private AssociatedIssue associatedIssue;
    private String body;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            return "{id: %s, userLogin: %s, owner:%s, name:%s}".formatted(getId(),
                    getUser().getLogin(), getRepository().getOwner(), getRepository().getName());
        }
    }

    /*
     *********************
     * Getters / Setters *
     *********************
     */

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(LocalDate lastEditedAt) {
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
