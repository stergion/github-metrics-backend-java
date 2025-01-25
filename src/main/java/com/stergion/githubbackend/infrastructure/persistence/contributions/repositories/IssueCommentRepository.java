package com.stergion.githubbackend.infrastructure.persistence.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.IssueComment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class IssueCommentRepository implements ContributionRepository<IssueComment> {
}
