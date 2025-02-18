package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.domain.contirbutions.repositories.CommitRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.CommitEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers.CommitMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.UUID;

public class CommitRepositoryAdapterPostgres implements CommitRepository {
    private final CommitRepositoryPostgres repository;
    private final CommitMapper mapper;

    public CommitRepositoryAdapterPostgres(CommitRepositoryPostgres repository,
                                           CommitMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<Commit> persist(Commit contribution) {
        CommitEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<Commit>> persist(List<Commit> contributions) {
        List<CommitEntity> entities = mapper.toEntity(contributions);
        return repository.persist(entities)
                         .map(ignored -> mapper.toDomain(entities));
    }

    @Override
    public Uni<Void> delete(Commit contribution) {
        CommitEntity entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Void> delete(List<Commit> contributions) {
        List<CommitEntity> entities = mapper.toEntity(contributions);
        return Multi.createFrom()
                    .iterable(entities)
                    .call(repository::delete)
                    .toUni().replaceWithVoid();
    }

    @Override
    public Uni<Commit> update(Commit contribution) {
        CommitEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Commit> findById(String id) {
        return repository.findById(UUID.fromString(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Commit> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Commit> findByUserId(String id) {
        return repository.findByUserId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Commit> findByRepoId(String id) {
        return repository.findByRepoId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Commit> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<Commit>> search(CommitSearchCriteria criteria) {
        // TODO: Implement search functionality
        throw new NotImplementedYet();
    }
}
