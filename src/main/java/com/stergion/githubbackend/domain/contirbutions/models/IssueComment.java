package com.stergion.githubbackend.domain.contirbutions.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.Github;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public non-sealed class IssueComment extends Contribution {
    @NotNull
    @PastOrPresent
    LocalDate createdAt;

    LocalDate publishedAt;
    LocalDate updatedAt;
    LocalDate lastEditedAt;
    AssociatedIssue associatedIssue;
    String body;

    private static final ObjectMapper mapper = JsonObjectMapper.create();

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

    public void setAssociatedIssue(AssociatedIssue associatedIssue) {
        this.associatedIssue = associatedIssue;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public record AssociatedIssue(IssueType type, Github github) {
        @Override
        public String toString() {
            return "{type: " + type + ", github: " + github + "}";
        }
    }

    public enum IssueType {
        ISSUE, PULL_REQUEST,
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(user, repository);
        }
    }
}
