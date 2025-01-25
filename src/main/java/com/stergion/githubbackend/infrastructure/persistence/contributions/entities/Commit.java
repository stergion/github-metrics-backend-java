package com.stergion.githubbackend.infrastructure.persistence.contributions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.*;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "commits")
public non-sealed class Commit implements Contribution {
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
    public LocalDate committedDate;
    public LocalDate pushedDate;
    public int additions;
    public int deletions;
    public List<CommitComment> comments;
    public int commentsCount;
    public List<Github> associatedPullRequest;
    public int associatedPullRequestsCount;
    public List<File> files;
    public int filesCount;

    @Override
    public String toString() {
        return "{ id: " + id +
                ", userId: " + userId +
                ", repositoryId: " + repositoryId +
                ", github: " + github +
                "committedDate: " + committedDate +
                ", pushedDate: " + pushedDate +
                ", additions: " + additions +
                ", deletions: " + deletions +
                ", comments: " + comments +
                ", commentsCount: " + commentsCount +
                ", associatedPullRequest: " + associatedPullRequest +
                ", associatedPullRequestsCount: " + associatedPullRequestsCount +
                ", files: " + files +
                ", filesCount: " + filesCount +
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

    public LocalDate committedDate() {
        return committedDate;
    }

    public LocalDate pushedDate() {
        return pushedDate;
    }

    public int additions() {
        return additions;
    }

    public int deletions() {
        return deletions;
    }

    public List<CommitComment> comments() {
        return comments;
    }

    public int commentsCount() {
        return commentsCount;
    }

    public List<Github> associatedPullRequest() {
        return associatedPullRequest;
    }

    public int associatedPullRequestsCount() {
        return associatedPullRequestsCount;
    }

    public List<File> files() {
        return files;
    }

    public int filesCount() {
        return filesCount;
    }
}
