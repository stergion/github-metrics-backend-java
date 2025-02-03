package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities;

import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.UserWithLogin;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueType;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@MongoEntity(collection = "issueComments")
public non-sealed class IssueComment implements Contribution {
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

    public LocalDate publishedAt;
    public LocalDate updatedAt;
    public LocalDate lastEditedAt;
    public AssociatedIssue associatedIssue;
    public String body;

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

    public LocalDate publishedAt() {
        return publishedAt;
    }

    public LocalDate updatedAt() {
        return updatedAt;
    }

    public LocalDate lastEditedAt() {
        return lastEditedAt;
    }

    public AssociatedIssue associatedIssue() {
        return associatedIssue;
    }

    public String body() {
        return body;
    }

    public static class AssociatedIssue {
        public IssueType type;
        public Github github;

        @Override
        public String toString() {
            return "AssociatedIssue{" +
                    "type=" + type +
                    ", github=" + github +
                    "}";
        }
    }

    @Override
    public String toString() {
        return "{ id: " + id +
                ", userId: " + userId +
                ", repositoryId: " + repositoryId +
                ", github: " + github +
                ", createdAt: " + createdAt +
                ", publishedAt: " + publishedAt +
                ", updatedAt: " + updatedAt +
                ", lastEditedAt: " + lastEditedAt +
                ", associatedIssue: " + associatedIssue +
                ", body: '" + body + '\'' +
                "}";
    }
}
