package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.ContributionEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.UUID;

public sealed interface ContributionRepository<T extends ContributionEntity>
        extends PanacheRepositoryBase<T, UUID>
        permits CommitRepository, IssueRepository, IssueCommentRepository, PullRequestRepository,
        PullRequestReviewRepository {

    default Uni<T> findById(UUID id) {
        return find("id", id).firstResult();
    }

    default Uni<T> findByGitHubId(String gitHubId) {
        return find("githubId", gitHubId).firstResult();
    }

    default Uni<List<T>> findByUserId(UUID userId) {
        return find("user.id", userId).list();
    }

    default Uni<List<T>> findByRepoId(UUID repositoryId) {
        return find("repository.id", repositoryId).list();
    }

    default Uni<List<T>> findByUserIdAndRepoId(UUID userId, UUID repositoryId) {
        return find("user.id = ?1 " + "and repository.id = ?2",
                userId, repositoryId).list();
    }

    // Specialized deletion methods
    default Uni<Long> delete(UUID userId) {
        return delete("user.id", userId);
    }

    @Override
    Uni<Long> deleteAll();
}
