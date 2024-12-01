package com.stergion.githubbackend.core.contirbutions.entities;

import com.stergion.githubbackend.core.utilityTypes.Github;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@MongoEntity(collection = "issueComments")
public non-sealed class IssueComment extends Contribution {
//    createdAt: Date;
//    publishedAt: Date;
//    updatedAt: Date | null;
//    lastEditedAt: Date | null;
//    associatedIssue: AssociatedIssue;
//    body: string;
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
        return  "{ id: " + id +
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

    @Override
    public final boolean equals(Object o) {
        return (o instanceof IssueComment ic)
                && userId.equals(ic.userId)
                && repositoryId.equals(ic.repositoryId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + repositoryId.hashCode();
        return result;
    }
}
