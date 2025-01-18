package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.*;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "pullRequests")
public non-sealed class PullRequest implements Contribution {
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
    @PastOrPresent
    public LocalDate createdAt;

    public LocalDate mergedAt;
    public LocalDate closedAt;
    public LocalDate updatedAt;
    public PullRequestState state;
    public int reactionsCount;
    public List<Label> labels;
    public String title;
    public String body;
    public List<PullRequestCommit> commits;
    public int commitsCount;
    public int commentsCount;
    public List<Github> closingIssuesReferences;
    public int closingIssuesReferencesCount;


    @Override
    public String toString() {
        return "{ id: " + id +
                ", userId: " + userId +
                ", repositoryId: " + repositoryId +
                ", github: " + github +
                ", createdAt: " + createdAt +
                ", mergedAt: " + mergedAt +
                ", closedAt: " + closedAt +
                ", updatedAt: " + updatedAt +
                ", state: " + state +
                ", reactionsCount: " + reactionsCount +
                ", labels: " + labels +
                ", title: '" + title + '\'' +
                ", body: '" + body + '\'' +
                ", commits: " + commits +
                ", commitsCount: " + commitsCount +
                ", commentsCount: " + commentsCount +
                ", closingIssuesReferences: " + closingIssuesReferences +
                ", closingIssuesReferencesCount: " + closingIssuesReferencesCount +
                "}";
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

    public LocalDate createdAt() {
        return createdAt;
    }

    public LocalDate mergedAt() {
        return mergedAt;
    }

    public LocalDate closedAt() {
        return closedAt;
    }

    public LocalDate updatedAt() {
        return updatedAt;
    }

    public PullRequestState state() {
        return state;
    }

    public int reactionsCount() {
        return reactionsCount;
    }

    public List<Label> labels() {
        return labels;
    }

    public String title() {
        return title;
    }

    public String body() {
        return body;
    }

    public List<PullRequestCommit> commits() {
        return commits;
    }

    public int commitsCount() {
        return commitsCount;
    }

    public int commentsCount() {
        return commentsCount;
    }

    public List<Github> closingIssuesReferences() {
        return closingIssuesReferences;
    }

    public int closingIssuesReferencesCount() {
        return closingIssuesReferencesCount;
    }
}
