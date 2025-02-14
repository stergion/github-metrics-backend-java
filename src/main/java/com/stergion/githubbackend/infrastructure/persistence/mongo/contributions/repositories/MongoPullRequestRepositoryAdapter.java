package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;


import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.PullRequestMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search.MongoPullRequestSearchStrategy;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import java.util.List;

public class MongoPullRequestRepositoryAdapter implements PullRequestRepository {
    private final MongoPullRequestRepository repository;
    private final PullRequestMapper mapper;
    private final MongoPullRequestSearchStrategy searchStrategy;

    public MongoPullRequestRepositoryAdapter(MongoPullRequestRepository repository,
                                             PullRequestMapper mapper,
                                             MongoPullRequestSearchStrategy searchStrategy) {
        this.repository = repository;
        this.mapper = mapper;
        this.searchStrategy = searchStrategy;
    }


    @Override
    public Uni<PullRequest> persist(PullRequest commit) {
        PullRequestEntity entity = mapper.toEntity(commit);
        return repository.persist(entity).map(mapper::toDomain);
    }

    @Override
    public Uni<Void> persist(List<PullRequest> contributions) {
        var entities = contributions.stream()
                                    .map(mapper::toEntity)
                                    .toList();
        return repository.persist(entities);
    }

    @Override
    public Uni<Void> delete(PullRequest contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Long> delete(List<PullRequest> contributions) {
        var ids = contributions.stream()
                               .map(mapper::toEntity)
                               .map(PullRequestEntity::id)
                               .toList();
        return repository.deleteById(ids);
    }

    @Override
    public Uni<PullRequest> update(PullRequest contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.update(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequest> findById(String id) {
        return repository.findById(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequest> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequest> findByUserId(String id) {
        return repository.findByUserId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequest> findByRepoId(String id) {
        return repository.findByRepoId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequest> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<PullRequest>> search(PullRequestSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     mapper::toDomain));
    }
}
