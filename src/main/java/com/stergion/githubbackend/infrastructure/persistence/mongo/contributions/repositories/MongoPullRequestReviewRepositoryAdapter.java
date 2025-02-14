package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;


import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestReviewRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestReviewEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.PullRequestReviewMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search.MongoPullRequestReviewSearchStrategy;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;

import java.util.List;

public class MongoPullRequestReviewRepositoryAdapter implements PullRequestReviewRepository {
    private final MongoPullRequestReviewRepository repository;
    private final PullRequestReviewMapper mapper;
    private final MongoPullRequestReviewSearchStrategy searchStrategy;

    public MongoPullRequestReviewRepositoryAdapter(MongoPullRequestReviewRepository repository,
                                                   PullRequestReviewMapper mapper,
                                                   MongoPullRequestReviewSearchStrategy searchStrategy) {
        this.repository = repository;
        this.mapper = mapper;
        this.searchStrategy = searchStrategy;
    }


    @Override
    public Uni<PullRequestReview> persist(PullRequestReview commit) {
        PullRequestReviewEntity entity = mapper.toEntity(commit);
        return repository.persist(entity).map(mapper::toDomain);
    }

    @Override
    public Uni<Void> persist(List<PullRequestReview> contributions) {
        var entities = contributions.stream()
                                    .map(mapper::toEntity)
                                    .toList();
        return repository.persist(entities);
    }

    @Override
    public Uni<Void> delete(PullRequestReview contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.delete(entity);
    }

    @Override
    public Uni<Long> delete(List<PullRequestReview> contributions) {
        var ids = contributions.stream()
                               .map(mapper::toEntity)
                               .map(PullRequestReviewEntity::id)
                               .toList();
        return repository.deleteById(ids);
    }

    @Override
    public Uni<PullRequestReview> update(PullRequestReview contribution) {
        var entity = mapper.toEntity(contribution);
        return repository.update(entity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequestReview> findById(String id) {
        return repository.findById(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<PullRequestReview> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequestReview> findByUserId(String id) {
        return repository.findByUserId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequestReview> findByRepoId(String id) {
        return repository.findByRepoId(new ObjectId(id))
                         .map(mapper::toDomain);
    }

    @Override
    public Multi<PullRequestReview> findByUserAndRepoId(String userId, String repoId) {
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
    public Uni<PagedResponse<PullRequestReview>> search(PullRequestReviewSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     mapper::toDomain));
    }
}
