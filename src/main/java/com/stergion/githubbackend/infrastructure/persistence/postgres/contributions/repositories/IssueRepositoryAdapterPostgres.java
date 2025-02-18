package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers.IssueMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.UUID;

public class IssueRepositoryAdapterPostgres implements IssueRepository {
    private final IssueRepositoryPostgres repository;
    private final IssueMapper mapper;

    public IssueRepositoryAdapterPostgres(IssueRepositoryPostgres repository,
                                          IssueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<Issue> persist(Issue contribution) {
        IssueEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<Issue>> persist(List<Issue> contributions) {
        List<IssueEntity> entities = mapper.toEntity(contributions);
        return repository.persist(entities)
                         .map(ignored -> mapper.toDomain(entities));
    }

    @Override
    public Uni<Void> delete(Issue contribution) {
        IssueEntity entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Void> delete(List<Issue> contributions) {
        List<IssueEntity> entities = mapper.toEntity(contributions);
        return Multi.createFrom()
                    .iterable(entities)
                    .call(repository::delete)
                    .toUni().replaceWithVoid();
    }

    @Override
    public Uni<Issue> update(Issue contribution) {
        IssueEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Issue> findById(String id) {
        return repository.findById(UUID.fromString(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Issue> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Issue> findByUserId(String id) {
        return repository.findByUserId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Issue> findByRepoId(String id) {
        return repository.findByRepoId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Issue> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<Issue>> search(IssueSearchCriteria criteria) {
        // TODO: Implement search functionality
        throw new NotImplementedYet();
    }
}
