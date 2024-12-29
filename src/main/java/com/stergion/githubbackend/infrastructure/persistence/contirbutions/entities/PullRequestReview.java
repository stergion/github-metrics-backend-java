package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.PullRequestReviewComment;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.PullRequestReviewState;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "pullRequestReviews")
public non-sealed class PullRequestReview extends Contribution {

    @NotNull
    public Github pullRequest;

    public Github github;

    @NotNull
    @PastOrPresent
    public LocalDate createdAt;

    public LocalDate submittedAt;
    public LocalDate updatedAt;
    public LocalDate publishedAt;
    public LocalDate lastEditedAt;
    public PullRequestReviewState state;
    public String body;
    public List<PullRequestReviewComment> comments;

    @Override
    public String toString() {
        return "{ id: " + id +
                ", userId: " + userId +
                ", repositoryId: " + repositoryId +
                ", github: " + github +
                ", pullRequest: " + pullRequest +
                ", github: " + github +
                ", createdAt: " + createdAt +
                ", submittedAt: " + submittedAt +
                ", updatedAt: " + updatedAt +
                ", publishedAt: " + publishedAt +
                ", lastEditedAt: " + lastEditedAt +
                ", state: " + state +
                ", body: '" + body + '\'' +
                ", comments: " + comments +
                '}';
    }
}
