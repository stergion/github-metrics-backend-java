package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.Github;
import com.stergion.githubbackend.utilityTypes.IssueState;
import com.stergion.githubbackend.utilityTypes.Label;
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
    public IssueState state;
    public int reactionsCount;
    public List<Label> labels;
    public String title;
    public String body;
    public List<Commit> commits;
    public int commitsCount;
    public int commentsCount;
    public List<Github> closingIssuesReferences;
    public int closingIssuesReferencesCount;

    @Override
    public String toString() {
        return  "{ id: " + id +
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

    @Override
    public final boolean equals(Object o) {
        return (o instanceof PullRequest pr)
                && userId.equals(pr.userId)
                && repositoryId.equals(pr.repositoryId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + repositoryId.hashCode();
        return result;
    }
}
