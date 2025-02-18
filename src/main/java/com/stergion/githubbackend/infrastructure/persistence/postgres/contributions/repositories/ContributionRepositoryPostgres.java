package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.ContributionEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.UUID;

public sealed interface ContributionRepositoryPostgres<T extends ContributionEntity>
        extends PanacheRepositoryBase<T, UUID>
        permits CommitRepositoryPostgres, IssueRepositoryPostgres, IssueCommentRepositoryPostgres
        , PullRequestRepositoryPostgres,
        PullRequestReviewRepositoryPostgres {

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
    default Uni<List<UUID>> getRepositoryIds(UUID userId) {
        return list(
                "SELECT DISTINCT repository.id from Contributions WHERE user.id = ?1",
                userId
                   )
                .map(list -> list.stream()
                                 .map(i -> i.getRepository().getId())
                                 .toList());
    }

    // Specialized deletion methods
    Uni<Long> deleteByUserId(UUID userId);

    @Override
    Uni<Long> deleteAll();
}
