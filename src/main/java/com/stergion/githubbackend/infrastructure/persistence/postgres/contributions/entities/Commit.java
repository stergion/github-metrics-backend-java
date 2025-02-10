package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.AssociatedPullRequest;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.CommitComment;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.File;
import io.quarkus.logging.Log;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Commits")
public class Commit extends ContributionEntity {

    @NotNull
    @PastOrPresent
    private LocalDateTime committedDate;

    private LocalDateTime pushedDate;

    private int additions;
    private int deletions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CommitComment> comments = new ArrayList<>();
    private int commentsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AssociatedPullRequest> associatedPullRequest = new ArrayList<>();
    private int associatedPullRequestsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<File> files = new ArrayList<>();
    private int filesCount;

    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            Log.error(e.getClass() + ": " + e.getMessage() + ". \nCause: " + e.getCause());
            return "{id: %s, userLogin: %s, owner:%s, name:%s}".formatted(getId(),
                    getUser().getLogin(), getRepository().getOwner(), getRepository().getName());
        }
    }

    /*
     *********************
     * Getters / Setters *
     *********************
     */

    public LocalDateTime getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(LocalDateTime committedDate) {
        this.committedDate = committedDate;
    }

    public LocalDateTime getPushedDate() {
        return pushedDate;
    }

    public void setPushedDate(LocalDateTime pushedDate) {
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
        return List.copyOf(comments);
    }

    public void setComments(List<CommitComment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<AssociatedPullRequest> getAssociatedPullRequest() {
        return List.copyOf(associatedPullRequest);
    }

    public void setAssociatedPullRequest(List<AssociatedPullRequest> associatedPullRequest) {
        this.associatedPullRequest.clear();
        this.associatedPullRequest.addAll(associatedPullRequest);
    }

    public int getAssociatedPullRequestsCount() {
        return associatedPullRequestsCount;
    }

    public void setAssociatedPullRequestsCount(int associatedPullRequestsCount) {
        this.associatedPullRequestsCount = associatedPullRequestsCount;
    }

    public List<File> getFiles() {
        return List.copyOf(files);
    }

    public void setFiles(List<File> files) {
        this.files.clear();
        this.files.addAll(files);
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }
}
