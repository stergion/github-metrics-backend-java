package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities;

import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.UserWithLogin;
import org.bson.types.ObjectId;

public sealed interface ContributionEntity permits CommitEntity, IssueEntity, PullRequestEntity, PullRequestReviewEntity,
        IssueCommentEntity {
    public ObjectId id();

    public ObjectId userId();

    public ObjectId repositoryId();

    public UserWithLogin user();

    public NameWithOwner repository();

    public Github github();
}
