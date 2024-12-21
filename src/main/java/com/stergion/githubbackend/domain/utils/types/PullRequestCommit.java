package com.stergion.githubbackend.domain.utils.types;

public record PullRequestCommit(Github github, int additions, int deletions, int changedFiles) {

    @Override
    public String toString() {
        return "{\"github\": %s, \"additions\": %s, \"deletions\": %s, \"changedFiles\": %s}".formatted(
                github, additions, deletions, changedFiles);
    }
}
