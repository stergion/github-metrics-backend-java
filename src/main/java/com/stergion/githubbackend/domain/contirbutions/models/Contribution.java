package com.stergion.githubbackend.domain.contirbutions.models;

import com.stergion.githubbackend.domain.utils.types.Github;
import jakarta.validation.constraints.NotNull;

public sealed abstract class Contribution
        permits Commit, Issue, PullRequest, PullRequestReview,
        IssueComment {
    String id;

    @NotNull
    UserProjection user;

    @NotNull
    RepositoryProjection repository;

    Github github;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserProjection getUser() {
        return user;
    }

    public void setUser(UserProjection user) {
        this.user = user;
    }

    public RepositoryProjection getRepository() {
        return repository;
    }

    public void setRepository(RepositoryProjection repository) {
        this.repository = repository;
    }

    public Github getGithub() {
        return github;
    }

    public void setGithub(Github github) {
        this.github = github;
    }
}
