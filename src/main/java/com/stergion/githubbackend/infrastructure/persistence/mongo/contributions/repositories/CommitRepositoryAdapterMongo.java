package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;


import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.domain.contirbutions.repositories.CommitRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.CommitEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.CommitMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search.CommitSearchStrategyMongo;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import java.util.List;

public class CommitRepositoryAdapterMongo implements CommitRepository {
    private final CommitRepositoryMongo repository;
    private final CommitMapper mapper;
    private final CommitSearchStrategyMongo searchStrategy;

    public CommitRepositoryAdapterMongo(CommitRepositoryMongo repository, CommitMapper mapper,
                                        CommitSearchStrategyMongo searchStrategy) {
        this.repository = repository;
        this.mapper = mapper;
        this.searchStrategy = searchStrategy;
    }


    @Override
    public Uni<Commit> persist(Commit commit) {
        CommitEntity entity = mapper.toEntity(commit);
        return repository.persist(entity).map(mapper::toDomain);
    }

    @Override
    public Uni<List<Commit>> persist(List<Commit> contributions) {
        var entities = contributions.stream()
                                    .map(mapper::toEntity)
                                    .toList();
        return repository.persist(entities)
                         .chain(ignored -> Uni.createFrom().item(entities))
                         .map(list -> list.stream().map(mapper::toDomain).toList());
    }

    @Override
    public Uni<Void> delete(Commit contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Void> delete(List<Commit> contributions) {
        var ids = contributions.stream()
                               .map(mapper::toEntity)
                               .map(CommitEntity::id)
                               .toList();
        return repository.deleteById(ids).replaceWithVoid();
    }

    @Override
    public Uni<Commit> update(Commit contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.update(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Commit> findById(String id) {
        return repository.findById(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Commit> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Commit> findByUserId(String id) {
        return repository.findByUserId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Commit> findByRepoId(String id) {
        return repository.findByRepoId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<Commit> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<Commit>> search(CommitSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     mapper::toDomain));
    }

}
