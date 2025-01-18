package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.*;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "pullRequestReviews")
public non-sealed class PullRequestReview implements Contribution {
        ObjectId id;

        @NotNull
        @BsonProperty("user_id")
        ObjectId userId;

        @NotNull
        @BsonProperty("repository_id")
        ObjectId repositoryId;

        @NotNull
        UserWithLogin user;
        @NotNull
        NameWithOwner repository;

        @NotNull
        Github github;

        @NotNull
        Github pullRequest;

        @NotNull
        @PastOrPresent
        LocalDate createdAt;

        LocalDate submittedAt;
        LocalDate updatedAt;
        LocalDate publishedAt;
        LocalDate lastEditedAt;
        PullRequestReviewState state;
        String body;
        List<PullRequestReviewComment> comments;


    @Override
    public String toString() {
        return "{ id: " + id +
                ", userId: " + userId +
                ", repositoryId: " + repositoryId +
                ", github: " + github +
                ", pullRequest: " + pullRequest +
                ", github: " + github +
                ", createdAt: " + createdAt +
                ", submittedAt: " + submittedAt +
                ", updatedAt: " + updatedAt +
                ", publishedAt: " + publishedAt +
                ", lastEditedAt: " + lastEditedAt +
                ", state: " + state +
                ", body: '" + body + '\'' +
                ", comments: " + comments +
                '}';
    }

    @Override
    public ObjectId id() {
        return id;
    }

    @Override
    public ObjectId userId() {
        return userId;
    }

    @Override
    public ObjectId repositoryId() {
        return repositoryId;
    }

    @Override
    public UserWithLogin user() {
        return user;
    }

    @Override
    public NameWithOwner repository() {
        return repository;
    }

    @Override
    public Github github() {
        return github;
    }

    public Github pullRequest() {
        return pullRequest;
    }

    public LocalDate createdAt() {
        return createdAt;
    }

    public LocalDate submittedAt() {
        return submittedAt;
    }

    public LocalDate updatedAt() {
        return updatedAt;
    }

    public LocalDate publishedAt() {
        return publishedAt;
    }

    public LocalDate lastEditedAt() {
        return lastEditedAt;
    }

    public PullRequestReviewState state() {
        return state;
    }

    public String body() {
        return body;
    }

    public List<PullRequestReviewComment> comments() {
        return comments;
    }
}
