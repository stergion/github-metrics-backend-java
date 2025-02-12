package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.models.Contribution;
import com.stergion.githubbackend.domain.contirbutions.models.RepositoryProjection;
import com.stergion.githubbackend.domain.contirbutions.models.UserProjection;
import com.stergion.githubbackend.domain.contirbutions.repositories.ContributionRepository;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.domain.users.UserService;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.ContributionEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Abstract base service that provides common processing logic for all contribution types.
 * Uses the Template Method pattern to allow specific contribution services to customize
 * only what they need to.
 *
 * @param <D> The domain model type (e.g., Issue, Commit)
 * @param <E> The Entity type (e.g., IssueEntity, CommitEntity)
 */
public abstract class ContributionService<D extends Contribution, E extends ContributionEntity> {
    @Inject
    UserService userService;

    @Inject
    RepositoryService repositoryService;

    protected ContributionRepository<D> repository;
    protected FetchStrategy<D> fetchStrategy;

    protected ContributionService() {
        // Empty constructor for CDI
    }

    protected ContributionService(ContributionRepository<D> repository,
                                  FetchStrategy<D> fetchStrategy) {
        this.repository = repository;
        this.fetchStrategy = fetchStrategy;
    }

    /**
     * Ensures all repositories referenced by the contributions exist in the database.
     * This is common functionality used by all contribution types.
     */
    private Uni<Void> ensureRepositoriesExist(List<D> batch) {
        return Multi.createFrom().iterable(batch)
                    .map(D::getRepository)
                    .select().distinct()
                    .onItem().transformToUniAndMerge(
                        repo -> Uni.createFrom().item(repo)
                                   .emitOn(Infrastructure.getDefaultWorkerPool())
                                   .map(r -> repositoryService.fetchAndCreateRepository(r.owner(),
                                           r.name()))
                                                    )
                    .collect().asList()
                    .replaceWithVoid();
    }

    /**
     * Processes a batch of contributions:
     * 1. Ensures referenced repositories exist
     * 2. Converts domain models to entities
     * 3. Persists entities
     * 4. Maps back to domain models for return
     */
    protected Uni<List<D>> createContributions(List<D> contributions) {
        return Multi.createFrom().item(contributions)
                    .onItem().transformToUniAndConcatenate(this::ensureRepositoriesExist)
                    .toUni()
                    .chain(ignored -> Uni.createFrom().item(contributions))
                    .call(item -> repository.persist(item));
    }

    /**
     * Get a single contribution by ID
     */
    public Uni<D> getContribution(ObjectId id) {
        return repository.findById(id.toHexString());

    }

    /**
     * Get all contributions for a user
     */
    public Multi<D> getUserContributions(String login) {
        ObjectId userId = userService.getUserId(login);
        return repository.findByUserId(userId.toHexString());
    }

    public Multi<D> getUserContributions(String login, String owner, String name) {
        ObjectId userId = userService.getUserId(login);
        ObjectId repoId = repositoryService.getRepositoryId(new NameWithOwner(owner, name));

        return repository.findByUserAndRepoId(userId.toHexString(), repoId.toHexString());
    }

    /**
     * Template method for fetching and creating contributions.
     * Uses the FetchStrategy to handle the fetching logic.
     */
    protected Multi<List<D>> fetchAndCreate(FetchParams params) {
        return fetchStrategy
                .fetchBatched(params, BatchProcessorConfig.defaultConfig())
                .map(this::createContributions)
                .flatMap(Uni::toMulti);
    }

    public Uni<List<ObjectId>> getRepositoryIds(ObjectId userId) {
        return repository.getRepositoryIds(userId.toHexString())
                         .map(list -> list.stream().map(ObjectId::new).toList());
    }

    public Uni<Void> deleteUserContributions(ObjectId userId) {
        return repository.deleteByUserId(userId.toHexString());
    }
}
