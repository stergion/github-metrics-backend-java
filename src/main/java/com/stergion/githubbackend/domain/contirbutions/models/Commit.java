package com.stergion.githubbackend.domain.contirbutions.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.CommitComment;
import com.stergion.githubbackend.domain.utils.types.File;
import com.stergion.githubbackend.domain.utils.types.Github;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public non-sealed class Commit extends Contribution {
    @NotNull
    @PastOrPresent
    LocalDate committedDate;
    LocalDate pushedDate;
    int additions;
    int deletions;
    final List<CommitComment> comments = new ArrayList<>();
    int commentsCount;
    final List<Github> associatedPullRequest = new ArrayList<>();
    int associatedPullRequestsCount;
    final List<File> files = new ArrayList<>();
    int filesCount;

    private static final ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(user, repository);
        }
    }

    public LocalDate getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(LocalDate committedDate) {
        this.committedDate = committedDate;
    }

    public LocalDate getPushedDate() {
        return pushedDate;
    }

    public void setPushedDate(LocalDate pushedDate) {
        this.pushedDate = pushedDate;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public void setComments(List<CommitComment> comments) {
        this.comments.clear();
        if (comments != null) {
            this.comments.addAll(comments);
        }
    }

    public List<CommitComment> getComments() {
        return comments;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public void setAssociatedPullRequest(List<Github> associatedPullRequest) {
        this.associatedPullRequest.clear();
        if (associatedPullRequest != null) {
            this.associatedPullRequest.addAll(associatedPullRequest);
        }
    }

    public List<Github> getAssociatedPullRequest() {
        return associatedPullRequest;
    }

    public int getAssociatedPullRequestsCount() {
        return associatedPullRequestsCount;
    }

    public void setAssociatedPullRequestsCount(int associatedPullRequestsCount) {
        this.associatedPullRequestsCount = associatedPullRequestsCount;
    }

    public void setFiles(List<File> files) {
        this.files.clear();
        if (files != null) {
            this.files.addAll(files);
        }
    }

    public List<File> getFiles() {
        return files;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }
}
