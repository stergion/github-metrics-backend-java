package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.Commit;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class CommitRepository implements ContributionRepository<Commit> {
}
