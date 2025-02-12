package com.stergion.githubbackend.domain.contirbutions.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public non-sealed class PullRequestReview extends Contribution {
    @NotNull
    Github pullRequest;

    @NotNull
    @PastOrPresent
    LocalDate createdAt;

    LocalDate submittedAt;
    LocalDate updatedAt;
    LocalDate publishedAt;
    LocalDate lastEditedAt;
    PullRequestReviewState state;
    String body;
    final List<PullRequestReviewComment> comments = new ArrayList<>();


    private static final ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(user, repository);
        }
    }

    public Github getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(Github pullRequest) {
        this.pullRequest = pullRequest;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDate submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDate getLastEditedAt() {
        return lastEditedAt;
    }

    public void setLastEditedAt(LocalDate lastEditedAt) {
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

    public void setComments(List<PullRequestReviewComment> comments) {
        this.comments.clear();
        if (comments != null) {
            this.comments.addAll(comments);
        }
    }

    public List<PullRequestReviewComment> getComments() {
        return comments;
    }
}
