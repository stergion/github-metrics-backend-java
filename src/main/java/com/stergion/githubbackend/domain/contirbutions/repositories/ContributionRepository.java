package com.stergion.githubbackend.domain.contirbutions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.Contribution;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.BaseSearchCriteria;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ContributionRepository<T extends Contribution, C extends BaseSearchCriteria<?, ?>> {
    Uni<T> persist(T contribution);

    Uni<List<T>> persist(List<T> contributions);

    Uni<Void> delete(T contribution);

    Uni<Void> delete(List<T> contributions);

    Uni<T> update(T contribution);

    Uni<T> findById(String id);

    Uni<T> findByGitHubId(String id);

    Multi<T> findByUserId(String id);

    Multi<T> findByRepoId(String id);

    Multi<T> findByUserAndRepoId(String userId, String repoId);

    Uni<List<String>> getRepositoryIds(String userId);

    Uni<Void> deleteByUserId(String id);

    Uni<PagedResponse<T>> search(C criteria);
}
