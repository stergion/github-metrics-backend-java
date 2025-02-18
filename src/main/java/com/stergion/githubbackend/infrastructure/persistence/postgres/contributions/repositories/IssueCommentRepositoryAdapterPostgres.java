package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueCommentRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueCommentEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers.IssueCommentMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;
import java.util.UUID;

public class IssueCommentRepositoryAdapterPostgres implements IssueCommentRepository {
    private final IssueCommentRepositoryPostgres repository;
    private final IssueCommentMapper mapper;

    public IssueCommentRepositoryAdapterPostgres(IssueCommentRepositoryPostgres repository,
                                                 IssueCommentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<IssueComment> persist(IssueComment contribution) {
        IssueCommentEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<IssueComment>> persist(List<IssueComment> contributions) {
        List<IssueCommentEntity> entities = mapper.toEntity(contributions);
        return repository.persist(entities)
                         .map(ignored -> mapper.toDomain(entities));
    }

    @Override
    public Uni<Void> delete(IssueComment contribution) {
        IssueCommentEntity entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Void> delete(List<IssueComment> contributions) {
        List<IssueCommentEntity> entities = mapper.toEntity(contributions);
        return Multi.createFrom()
                    .iterable(entities)
                    .call(repository::delete)
                    .toUni().replaceWithVoid();
    }

    @Override
    public Uni<IssueComment> update(IssueComment contribution) {
        IssueCommentEntity entity = mapper.toEntity(contribution);
        return repository.persist(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<IssueComment> findById(String id) {
        return repository.findById(UUID.fromString(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<IssueComment> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<IssueComment> findByUserId(String id) {
        return repository.findByUserId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<IssueComment> findByRepoId(String id) {
        return repository.findByRepoId(UUID.fromString(id))
                         .onItem().transformToMulti(Multi.createFrom()::iterable)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<IssueComment> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<IssueComment>> search(IssueCommentSearchCriteria criteria) {
        // TODO: Implement search functionality
        throw new NotImplementedYet();
    }
}
