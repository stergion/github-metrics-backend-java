package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.UserWithLogin;
import org.bson.types.ObjectId;

public sealed interface Contribution permits Commit, Issue, PullRequest, PullRequestReview,
        IssueComment {
    public ObjectId id();

    public ObjectId userId();

    public ObjectId repositoryId();

    public UserWithLogin user();

    public NameWithOwner repository();

    public Github github();
}
