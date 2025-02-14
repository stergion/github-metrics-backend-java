package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class MongoIssueCommentRepository
        implements MongoContributionRepository<IssueCommentEntity> {
}
