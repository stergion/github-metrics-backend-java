package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Commit;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class CommitRepository implements ContributionRepository<Commit> {
}
