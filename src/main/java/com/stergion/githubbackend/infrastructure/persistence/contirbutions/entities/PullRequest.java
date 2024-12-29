package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Label;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.PullRequestCommit;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.PullRequestState;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "pullRequests")
public non-sealed class PullRequest extends Contribution {
    @NotNull
    @PastOrPresent
    public LocalDate createdAt;

    public LocalDate mergedAt;
    public LocalDate closedAt;
    public LocalDate updatedAt;
    public PullRequestState state;
    public int reactionsCount;
    public List<Label> labels;
    public String title;
    public String body;
    public List<PullRequestCommit> commits;
    public int commitsCount;
    public int commentsCount;
    public List<Github> closingIssuesReferences;
    public int closingIssuesReferencesCount;

    @Override
    public String toString() {
        return "{ id: " + id +
                ", userId: " + userId +
                ", repositoryId: " + repositoryId +
                ", github: " + github +
                ", createdAt: " + createdAt +
                ", mergedAt: " + mergedAt +
                ", closedAt: " + closedAt +
                ", updatedAt: " + updatedAt +
                ", state: " + state +
                ", reactionsCount: " + reactionsCount +
                ", labels: " + labels +
                ", title: '" + title + '\'' +
                ", body: '" + body + '\'' +
                ", commits: " + commits +
                ", commitsCount: " + commitsCount +
                ", commentsCount: " + commentsCount +
                ", closingIssuesReferences: " + closingIssuesReferences +
                ", closingIssuesReferencesCount: " + closingIssuesReferencesCount +
                "}";
    }
}
