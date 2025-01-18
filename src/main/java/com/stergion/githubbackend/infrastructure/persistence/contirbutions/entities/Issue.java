package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.*;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "issues")
public non-sealed class Issue implements Contribution {
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
    @PastOrPresent
    LocalDate createdAt;
    LocalDate closedAt;
    LocalDate updatedAt;
    IssueState state;
    String title;
    String body;
    int reactionsCount;
    List<Label> labels;
    String closer;

    @Override
    public String toString() {
        return "{ id: " + id +
                ", userId: " + userId +
                ", repositoryId: " + repositoryId +
                ", github: " + github +
                ", createdAt: " + createdAt +
                ", closedAt: " + closedAt +
                ", updatedAt: " + updatedAt +
                ", state: " + state +
                ", title: '" + title + '\'' +
                ", body: '" + body + '\'' +
                ", reactionsCount: " + reactionsCount +
                ", labels: " + labels +
                ", closer: '" + closer + '\'' +
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

    public LocalDate closedAt() {
        return closedAt;
    }

    public LocalDate updatedAt() {
        return updatedAt;
    }

    public IssueState state() {
        return state;
    }

    public String title() {
        return title;
    }

    public String body() {
        return body;
    }

    public int reactionsCount() {
        return reactionsCount;
    }

    public List<Label> labels() {
        return labels;
    }

    public String closer() {
        return closer;
    }
}
