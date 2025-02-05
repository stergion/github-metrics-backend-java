package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities;

import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.*;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestReviewState;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "pullRequestReviews")
public non-sealed class PullRequestReview implements Contribution {
        public ObjectId id;

        @NotNull
        @BsonProperty("user_id")
        public ObjectId userId;

        @NotNull
        @BsonProperty("repository_id")
        public ObjectId repositoryId;

        @NotNull
        public UserWithLogin user;

        @NotNull
        public NameWithOwner repository;

        @NotNull
        public Github github;

        @NotNull
        public Github pullRequest;

        @NotNull
        @PastOrPresent
        public LocalDate createdAt;

        public LocalDate submittedAt;
        public LocalDate updatedAt;
        public LocalDate publishedAt;
        public LocalDate lastEditedAt;
        public PullRequestReviewState state;
        public String body;
        public List<PullRequestReviewComment> comments;


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
