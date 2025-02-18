package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestReviewRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestReviewEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers.PullRequestReviewMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.UUID;

public class PullRequestReviewRepositoryAdapterPostgres implements PullRequestReviewRepository {
    private final PullRequestReviewRepositoryPostgres repository;
    private final PullRequestReviewMapper mapper;

    public PullRequestReviewRepositoryAdapterPostgres(
            PullRequestReviewRepositoryPostgres repository,
            PullRequestReviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<PullRequestReview> persist(PullRequestReview contribution) {
        PullRequestReviewEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<PullRequestReview>> persist(List<PullRequestReview> contributions) {
        List<PullRequestReviewEntity> entities = mapper.toEntity(contributions);
        return repository.persist(entities)
                         .map(ignored -> mapper.toDomain(entities));
    }

    @Override
    public Uni<Void> delete(PullRequestReview contribution) {
        PullRequestReviewEntity entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Void> delete(List<PullRequestReview> contributions) {
        List<PullRequestReviewEntity> entities = mapper.toEntity(contributions);
        return Multi.createFrom()
                    .iterable(entities)
                    .call(repository::delete)
                    .toUni().replaceWithVoid();
    }

    @Override
    public Uni<PullRequestReview> update(PullRequestReview contribution) {
        PullRequestReviewEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequestReview> findById(String id) {
        return repository.findById(UUID.fromString(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequestReview> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequestReview> findByUserId(String id) {
        return repository.findByUserId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequestReview> findByRepoId(String id) {
        return repository.findByRepoId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequestReview> findByUserAndRepoId(String userId, String repoId) {
        return repository.findByUserIdAndRepoId(
                                 UUID.fromString(userId),
                                 UUID.fromString(repoId)
                                               )
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<String>> getRepositoryIds(String userId) {
        return repository.getRepositoryIds(UUID.fromString(userId))
                         .map(list -> list.stream().map(UUID::toString).toList());
    }

    @Override
    public Uni<Void> deleteByUserId(String id) {
        return findByUserId(id)
                .call(this::delete)
                .toUni().replaceWithVoid();
    }

    @Override
    public Uni<PagedResponse<PullRequestReview>> search(PullRequestReviewSearchCriteria criteria) {
        // TODO: Implement search functionality
        throw new NotImplementedYet();
    }
}
