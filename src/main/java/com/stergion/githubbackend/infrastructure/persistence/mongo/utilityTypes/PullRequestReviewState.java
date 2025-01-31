package com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes;

public enum PullRequestReviewState {
    PENDING,
    COMMENTED,
    APPROVED,
    CHANGES_REQUESTED,
    DISMISSED,
}
