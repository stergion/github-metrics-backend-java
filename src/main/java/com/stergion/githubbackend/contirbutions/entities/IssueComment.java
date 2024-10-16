package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.Github;
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
    }
    public enum IssueType {
        ISSUE,
        PULL_REQUEST,
    }
}
