package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities;

import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.UserWithLogin;
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
