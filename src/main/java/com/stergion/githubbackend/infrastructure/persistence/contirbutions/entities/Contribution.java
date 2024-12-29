package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.UserWithLogin;
import jakarta.validation.constraints.NotNull;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public sealed abstract class Contribution permits Commit, Issue, PullRequest, PullRequestReview,
        IssueComment {
    @NotNull
    @BsonId
    public ObjectId id;

    @NotNull
    @BsonProperty("user_id")
    public ObjectId userId;

    @NotNull
    @BsonProperty("repository_id")
    public ObjectId repositoryId;

    public UserWithLogin user;

    public NameWithOwner repository;

    public Github github;

    @Override
    public final boolean equals(Object o) {
        return (o instanceof Commit c)
                && id.equals(c.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
