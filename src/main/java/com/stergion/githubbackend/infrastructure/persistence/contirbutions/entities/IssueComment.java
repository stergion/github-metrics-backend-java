package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Github;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@MongoEntity(collection = "issueComments")
public non-sealed class IssueComment extends Contribution {
    @NotNull
    @PastOrPresent
    public LocalDate createdAt;

    public LocalDate publishedAt;
    public LocalDate updatedAt;
    public LocalDate lastEditedAt;
    public AssociatedIssue associatedIssue;
    public String body;

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
    public enum IssueType {
        ISSUE,
        PULL_REQUEST,
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
