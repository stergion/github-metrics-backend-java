package com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.CommitComment;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.File;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Github;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

@MongoEntity(collection = "commits")
public non-sealed class Commit extends Contribution {

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
    public final boolean equals(Object o) {
        return (o instanceof Commit c)
                && userId.equals(c.userId)
                && repositoryId.equals(c.repositoryId);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + repositoryId.hashCode();
        return result;
    }

}
