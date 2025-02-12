package com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types;

public class PullRequestCommit {
    public Github github;

    public int additions;

    public int deletions;

    public int changedFiles;

    @Override
    public String toString() {
        return "{\"github\": %s, \"additions\": %s, \"deletions\": %s, \"changedFiles\": %s}".formatted(github, additions, deletions, changedFiles);
    }
}
