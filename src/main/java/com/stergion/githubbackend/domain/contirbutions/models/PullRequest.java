package com.stergion.githubbackend.domain.contirbutions.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.domain.utils.types.PullRequestCommit;
import com.stergion.githubbackend.domain.utils.types.PullRequestState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public non-sealed class PullRequest extends Contribution {
    @NotNull
    @PastOrPresent
    LocalDate createdAt;

    LocalDate mergedAt;
    LocalDate closedAt;
    LocalDate updatedAt;
    PullRequestState state;
    int reactionsCount;
    final List<Label> labels = new ArrayList<>();
    String title;
    String body;
    final List<PullRequestCommit> commits = new ArrayList<>();
    int commitsCount;
    int commentsCount;
    final List<Github> closingIssuesReferences = new ArrayList<>();
    int closingIssuesReferencesCount;

    private static final ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(user, repository);
        }
    }

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

    public void setState(PullRequestState state) {
        this.state = state;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public void setReactionsCount(int reactionsCount) {
        this.reactionsCount = reactionsCount;
    }

    public void setLabels(List<Label> labels) {
        this.labels.clear();
        if (labels != null) {
            this.labels.addAll(labels);
        }
    }

    public List<Label> getLabels() {
        return labels;
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

    public void setCommits(List<PullRequestCommit> commits) {
        this.commits.clear();
        if (commits != null) {
            this.commits.addAll(commits);
        }
    }

    public List<PullRequestCommit> getCommits() {
        return commits;
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

    public void setClosingIssuesReferences(List<Github> closingIssuesReferences) {
        this.closingIssuesReferences.clear();
        if (closingIssuesReferences != null) {
            this.closingIssuesReferences.addAll(closingIssuesReferences);
        }
    }

    public List<Github> getClosingIssuesReferences() {
        return closingIssuesReferences;
    }

    public int getClosingIssuesReferencesCount() {
        return closingIssuesReferencesCount;
    }

    public void setClosingIssuesReferencesCount(int closingIssuesReferencesCount) {
        this.closingIssuesReferencesCount = closingIssuesReferencesCount;
    }
}
