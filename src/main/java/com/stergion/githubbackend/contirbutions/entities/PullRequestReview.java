package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.Github;
import com.stergion.githubbackend.utilityTypes.PullRequestReviewComment;
import com.stergion.githubbackend.utilityTypes.PullRequestReviewState;
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

}
