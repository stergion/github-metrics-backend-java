package com.stergion.githubbackend.domain.contirbutions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.domain.contirbutions.models.Contribution;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ContributionRepository<T extends Contribution> {
    Uni<T> persist(T contribution);

    Uni<Void> persist(List<T> contributions);

    Uni<Void> delete(T contribution);

    Uni<Long> delete(List<T> contributions);

    Uni<T> update(T contribution);

    Uni<T> findById(String id);

    Uni<T> findByGitHubId(String id);

    Multi<T> findByUserId(String id);

    Multi<T> findByRepoId(String id);

    Multi<T> findByUserAndRepoId(String userId, String repoId);

    Uni<List<String>> getRepositoryIds(String userId);

    Uni<Void> deleteByUserId(String id);
}
