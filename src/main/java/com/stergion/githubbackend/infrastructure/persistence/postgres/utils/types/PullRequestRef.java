package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import jakarta.persistence.Embeddable;

@Embeddable
public record PullRequestRef(String githubId, String githubUrl) {
    @Override
    public String toString() {
        return "{ githubId: %s, githubUrl: %s }".formatted(githubId, githubUrl);
    }
}
