package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.Github;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public sealed class Contribution permits Commit, Issue, PullRequest, PullRequestReview,
        IssueComment {
    public ObjectId id;
    @BsonProperty("user_id")
    public ObjectId userId;
    @BsonProperty("repository_id")
    public ObjectId repositoryId;
    public Github github;
}
