package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;


import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.IssueMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search.IssueSearchStrategyMongo;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import java.util.List;

public class IssueRepositoryAdapterMongo implements IssueRepository {
    private final IssueRepositoryMongo repository;
    private final IssueMapper mapper;
    private final IssueSearchStrategyMongo searchStrategy;

    public IssueRepositoryAdapterMongo(IssueRepositoryMongo repository, IssueMapper mapper,
                                       IssueSearchStrategyMongo searchStrategy) {
        this.repository = repository;
        this.mapper = mapper;
        this.searchStrategy = searchStrategy;
    }


    @Override
    public Uni<Issue> persist(Issue commit) {
        IssueEntity entity = mapper.toEntity(commit);
        return repository.persist(entity).map(mapper::toDomain);
    }

    @Override
    public Uni<List<Issue>> persist(List<Issue> contributions) {
        var entities = contributions.stream()
                                    .map(mapper::toEntity)
                                    .toList();
        return repository.persist(entities)
                         .chain(ignored -> Uni.createFrom().item(entities))
                         .map(list -> list.stream().map(mapper::toDomain).toList());
    }

    @Override
    public Uni<Void> delete(Issue contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Void> delete(List<Issue> contributions) {
        var ids = contributions.stream()
                               .map(mapper::toEntity)
                               .map(IssueEntity::id)
                               .toList();
        return repository.deleteById(ids).replaceWithVoid();
    }

    @Override
    public Uni<Issue> update(Issue contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.update(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Issue> findById(String id) {
        return repository.findById(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Issue> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Issue> findByUserId(String id) {
        return repository.findByUserId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Issue> findByRepoId(String id) {
        return repository.findByRepoId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Issue> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<Issue>> search(IssueSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     mapper::toDomain));
    }
}
