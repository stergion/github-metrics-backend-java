package com.stergion.githubbackend.contirbutions.entities;

import com.stergion.githubbackend.utilityTypes.CommitComment;
import com.stergion.githubbackend.utilityTypes.File;
import com.stergion.githubbackend.utilityTypes.Github;
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

}
