package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.ClosingIssuesReference;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Label;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.PullRequestCommit;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestState;
import io.quarkus.logging.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PullRequests")
public class PullRequestEntity extends ContributionEntity {

    @NotNull
    @PastOrPresent
    private LocalDateTime createdAt;

    private LocalDateTime mergedAt;
    private LocalDateTime closedAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private PullRequestState state;

    private int reactionsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Label> labels = new ArrayList<>();
    private String title;
    private String body;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PullRequestCommit> commits = new ArrayList<>();

    private int commitsCount;
    private int commentsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ClosingIssuesReference> closingIssuesReferences = new ArrayList<>();

    private int closingIssuesReferencesCount;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(LocalDateTime mergedAt) {
        this.mergedAt = mergedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PullRequestState getState() {
        return state;
    }

    public void setState(
            PullRequestState state) {
        this.state = state;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public void setReactionsCount(int reactionsCount) {
        this.reactionsCount = reactionsCount;
    }

    public List<Label> getLabels() {
        return List.copyOf(labels);
    }

    public void setLabels(List<Label> labels) {
        this.labels.clear();
        this.labels.addAll(labels);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<PullRequestCommit> getCommits() {
        return List.copyOf(commits);
    }

    public void setCommits(List<PullRequestCommit> commits) {
        this.commits.clear();
        this.commits.addAll(commits);
    }

    public int getCommitsCount() {
        return commitsCount;
    }

    public void setCommitsCount(int commitsCount) {
        this.commitsCount = commitsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<ClosingIssuesReference> getClosingIssuesReferences() {
        return List.copyOf(closingIssuesReferences);
    }

    public void setClosingIssuesReferences(List<ClosingIssuesReference> closingIssuesReferences) {
        this.closingIssuesReferences.clear();
        this.closingIssuesReferences.addAll(closingIssuesReferences);
    }

    public int getClosingIssuesReferencesCount() {
        return closingIssuesReferencesCount;
    }

    public void setClosingIssuesReferencesCount(int closingIssuesReferencesCount) {
        this.closingIssuesReferencesCount = closingIssuesReferencesCount;
    }
}
