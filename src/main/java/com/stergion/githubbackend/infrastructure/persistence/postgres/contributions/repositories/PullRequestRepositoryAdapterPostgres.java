package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers.PullRequestMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.UUID;

public class PullRequestRepositoryAdapterPostgres implements PullRequestRepository {
    private final PullRequestRepositoryPostgres repository;
    private final PullRequestMapper mapper;

    public PullRequestRepositoryAdapterPostgres(PullRequestRepositoryPostgres repository,
                                                PullRequestMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<PullRequest> persist(PullRequest contribution) {
        PullRequestEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<PullRequest>> persist(List<PullRequest> contributions) {
        List<PullRequestEntity> entities = mapper.toEntity(contributions);
        return repository.persist(entities)
                         .map(ignored -> mapper.toDomain(entities));
    }

    @Override
    public Uni<Void> delete(PullRequest contribution) {
        PullRequestEntity entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Void> delete(List<PullRequest> contributions) {
        List<PullRequestEntity> entities = mapper.toEntity(contributions);
        return Multi.createFrom()
                    .iterable(entities)
                    .call(repository::delete)
                    .toUni().replaceWithVoid();
    }

    @Override
    public Uni<PullRequest> update(PullRequest contribution) {
        PullRequestEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequest> findById(String id) {
        return repository.findById(UUID.fromString(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequest> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequest> findByUserId(String id) {
        return repository.findByUserId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequest> findByRepoId(String id) {
        return repository.findByRepoId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequest> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<PullRequest>> search(PullRequestSearchCriteria criteria) {
        // TODO: Implement search functionality
        throw new NotImplementedYet();
    }
}
