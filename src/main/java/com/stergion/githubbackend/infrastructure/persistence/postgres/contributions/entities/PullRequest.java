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

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PullRequests")
public class PullRequest extends Contribution {

    @NotNull
    @PastOrPresent
    private LocalDate createdAt;

    private LocalDate mergedAt;
    private LocalDate closedAt;
    private LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    private PullRequestState state;

    private int reactionsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "pullRequestId")
    private List<Label> labels;
    private String title;
    private String body;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pullRequestId")
    private List<PullRequestCommit> commits;

    private int commitsCount;
    private int commentsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pullRequestId")
    private List<ClosingIssuesReference> closingIssuesReferences;

    private int closingIssuesReferencesCount;

    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            Log.error(e.getClass()+ ": " + e.getMessage() + ". \nCause: " + e.getCause());
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

    public LocalDate getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(LocalDate mergedAt) {
        this.mergedAt = mergedAt;
    }

    public LocalDate getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDate closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
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
        return labels;
    }

    public void setLabels(
            List<Label> labels) {
        this.labels = labels;
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
        return commits;
    }

    public void setCommits(
            List<PullRequestCommit> commits) {
        this.commits = commits;
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
        return closingIssuesReferences;
    }

    public void setClosingIssuesReferences(
            List<ClosingIssuesReference> closingIssuesReferences) {
        this.closingIssuesReferences = closingIssuesReferences;
    }

    public int getClosingIssuesReferencesCount() {
        return closingIssuesReferencesCount;
    }

    public void setClosingIssuesReferencesCount(int closingIssuesReferencesCount) {
        this.closingIssuesReferencesCount = closingIssuesReferencesCount;
    }
}
