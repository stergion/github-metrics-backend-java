package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.Github;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public sealed class Contribution permits Commit, Issue, PullRequest, PullRequestReview,
        IssueComment {
    @NotNull
    public ObjectId id;

    @NotNull
    @BsonProperty("user_id")
    public ObjectId userId;

    @NotNull
    @BsonProperty("repository_id")
    public ObjectId repositoryId;

    public Github github;
}
