package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueComment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class IssueCommentRepository implements ContributionRepository<IssueComment> {
}
