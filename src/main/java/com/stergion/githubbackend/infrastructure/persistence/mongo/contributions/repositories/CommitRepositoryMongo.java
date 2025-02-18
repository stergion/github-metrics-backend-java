package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.CommitEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class CommitRepositoryMongo implements ContributionRepositoryMongo<CommitEntity> {
}
