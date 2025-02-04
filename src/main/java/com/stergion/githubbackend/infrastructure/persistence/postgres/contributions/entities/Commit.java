package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.AssociatedPullRequest;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.CommitComment;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.File;
import io.quarkus.logging.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Commits")
public class Commit extends Contribution {

    @NotNull
    @PastOrPresent
    private LocalDate committedDate;

    private LocalDate pushedDate;

    private int additions;
    private int deletions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commitId")
    private List<CommitComment> comments = new ArrayList<>();
    private int commentsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commitId")
    private List<AssociatedPullRequest> associatedPullRequest = new ArrayList<>();
    private int associatedPullRequestsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commitId")
    private List<File> files = new ArrayList<>();
    private int filesCount;

    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            Log.error(e.getClass()+ ": " + e.getMessage() + ". \nCause: " + e.getCause());
            return "{id: %s, userLogin: %s, owner:%s, name:%s}".formatted(getId(),
                    getUser().getLogin(), getRepository().getOwner(), getRepository().getName());
        }
    }

    /*
     *********************
     * Getters / Setters *
     *********************
     */

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

    public List<CommitComment> getComments() {
        return comments;
    }

    public void setComments(
            List<CommitComment> comments) {
        this.comments = comments;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<AssociatedPullRequest> getAssociatedPullRequest() {
        return associatedPullRequest;
    }

    public void setAssociatedPullRequest(
            List<AssociatedPullRequest> associatedPullRequest) {
        this.associatedPullRequest = associatedPullRequest;
    }

    public int getAssociatedPullRequestsCount() {
        return associatedPullRequestsCount;
    }

    public void setAssociatedPullRequestsCount(int associatedPullRequestsCount) {
        this.associatedPullRequestsCount = associatedPullRequestsCount;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(
            List<File> files) {
        this.files = files;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }
}
