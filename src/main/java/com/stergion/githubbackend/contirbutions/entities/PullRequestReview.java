package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.Github;
import com.stergion.githubbackend.utilityTypes.PullRequestReviewComment;
import com.stergion.githubbackend.utilityTypes.PullRequestReviewState;

import java.time.LocalDate;
import java.util.List;

public non-sealed class PullRequestReview extends Contribution{

    public Github pullRequest;
    public Github github;
    public LocalDate createdAt;
    public LocalDate submittedAt;
    public LocalDate updatedAt;
    public LocalDate publishedAt;
    public LocalDate lastEditedAt;
    public PullRequestReviewState state;
    public String body;
    public List<PullRequestReviewComment> comments;

}
