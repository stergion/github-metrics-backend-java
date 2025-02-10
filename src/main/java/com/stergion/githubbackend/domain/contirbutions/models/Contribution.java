package com.stergion.githubbackend.domain.contirbutions.models;

import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

public sealed interface Contribution
        permits Commit, Issue, PullRequest, PullRequestReview,
        IssueComment {
    ObjectId id();

    @NotNull
    String user();

    @NotNull
    NameWithOwner repository();

    Github github();
}
