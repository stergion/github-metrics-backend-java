package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record AssociatedIssue(
        @Enumerated(EnumType.STRING)
        IssueType type,
        String githubId,
        String githubUrl
) {
    @Override
    public String toString() {
        return "{ type: %s, githubId: %s, githubUrl: %s }".formatted(type, githubId, githubUrl);
    }
}
