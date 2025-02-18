package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;


import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueCommentRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.IssueCommentMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search.IssueCommentSearchStrategyMongo;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

public class IssueCommentRepositoryAdapterMongo implements IssueCommentRepository {
    @Inject
    IssueCommentRepositoryMongo repository;

    @Inject
    IssueCommentMapper mapper;

    @Inject
    IssueCommentSearchStrategyMongo searchStrategy;

    public IssueCommentRepositoryAdapterMongo(IssueCommentRepositoryMongo repository,
                                              IssueCommentMapper mapper,
                                              IssueCommentSearchStrategyMongo searchStrategy) {
        this.repository = repository;
        this.mapper = mapper;
        this.searchStrategy = searchStrategy;
    }


    @Override
    public Uni<IssueComment> persist(IssueComment commit) {
        IssueCommentEntity entity = mapper.toEntity(commit);
        return repository.persist(entity).map(mapper::toDomain);
    }

    @Override
    public Uni<List<IssueComment>> persist(List<IssueComment> contributions) {
        var entities = contributions.stream()
                                    .map(mapper::toEntity)
                                    .toList();
        return repository.persist(entities)
                         .chain(ignored -> Uni.createFrom().item(entities))
                         .map(list -> list.stream().map(mapper::toDomain).toList());
    }

    @Override
    public Uni<Void> delete(IssueComment contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Long> delete(List<IssueComment> contributions) {
        var ids = contributions.stream()
                               .map(mapper::toEntity)
                               .map(IssueCommentEntity::id)
                               .toList();
        return repository.deleteById(ids);
    }

    @Override
    public Uni<IssueComment> update(IssueComment contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.update(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<IssueComment> findById(String id) {
        return repository.findById(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<IssueComment> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<IssueComment> findByUserId(String id) {
        return repository.findByUserId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<IssueComment> findByRepoId(String id) {
        return repository.findByRepoId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<IssueComment> findByUserAndRepoId(String userId, String repoId) {
        return repository.findByUserAndRepoId(new ObjectId(userId), new ObjectId(repoId))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<String>> getRepositoryIds(String userId) {
        return repository.getRepositoryIds(new ObjectId(userId))
                         .map(list -> list.stream()
                                          .map(ObjectId::toHexString)
                                          .toList());
    }

    @Override
    public Uni<Void> deleteByUserId(String id) {
        return repository.deleteByUserId(new ObjectId(id));
    }

    @Override
    public Uni<PagedResponse<IssueComment>> search(IssueCommentSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     mapper::toDomain));
    }
}
