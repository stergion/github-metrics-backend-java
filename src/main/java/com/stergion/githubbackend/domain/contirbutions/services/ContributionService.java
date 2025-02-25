package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.models.Contribution;
import com.stergion.githubbackend.domain.contirbutions.models.RepositoryProjection;
import com.stergion.githubbackend.domain.contirbutions.models.UserProjection;
import com.stergion.githubbackend.domain.contirbutions.repositories.ContributionRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.BaseSearchCriteria;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.domain.users.UserService;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Abstract base service that provides common processing logic for all contribution types.
 * Uses the Template Method pattern to allow specific contribution services to customize
 * only what they need to.
 *
 * @param <D> The domain model type (e.g., Issue, Commit)
 * @param <C> The search criteria type (e.g., CommitSearchCriteria, IssueSearchCriteria)
 */
public abstract class ContributionService<D extends Contribution,
        C extends BaseSearchCriteria<?, ?>> {

    protected ContributionRepository<D, C> repository;
    protected FetchStrategy<D> fetchStrategy;

    @Inject
    UserService userService;
    @Inject
    RepositoryService repositoryService;

    protected ContributionService() {
        // Empty constructor for CDI
    }

    protected ContributionService(ContributionRepository<D, C> repository,
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
                    .chain(this::injectUserRef)
                    .chain(this::injectRepositoryRef)
                    .flatMap(item -> repository.persist(item));
    }

    private Uni<List<D>> injectRepositoryRef(List<D> batch) {
        if (batch.isEmpty()) {
            return Uni.createFrom().item(batch);
        }

        return Multi.createFrom().iterable(batch)
                    .onItem().transform(contribution -> {
                    var repo = repositoryService.getRepository(
                            contribution.getRepository().owner(),
                            contribution.getRepository().name()
                    );
                    contribution.setRepository(
                            new RepositoryProjection(repo.id(), repo.owner(), repo.name()));
                    return contribution;
                })
                    .collect().asList();
    }

    private Uni<List<D>> injectUserRef(List<D> batch) {
        if (batch.isEmpty()) {
            return Uni.createFrom().item(batch);
        }

        return Multi.createFrom().iterable(batch)
                    .onItem()
                    .transformToUniAndMerge(
                            contr -> userService.getUser(contr.getUser().login())
                                                .map(user -> {
                                                    contr.setUser(new UserProjection(user.id(),
                                                                                     user.login(),
                                                                                     user.name()));
                                                    return contr;
                                                })
                    )
                    .collect().asList();
    }

    /**
     * Get a single contribution by ID
     */
    public Uni<D> getContribution(String id) {
        return repository.findById(id);

    }

    /**
     * Get all contributions for a user
     */
    public Multi<D> getUserContributions(String login) {
//        String userId = userService.getUserId(login);
//        return repository.findByUserId(userId);
        return userService.getUserId(login)
                          .onItem().transformToMulti(repository::findByUserId);
    }

    public Multi<D> getUserContributions(String login, String owner, String name) {
//        String userId = userService.getUserId(login);
//        String repoId = repositoryService.getRepositoryId(new NameWithOwner(owner, name));
//
//        return repository.findByUserAndRepoId(userId, repoId);
        return Uni.combine().all().unis(
                userService.getUserId(login),
                Uni.createFrom().item(repositoryService.getRepositoryId(new NameWithOwner(owner, name)))
                ).asTuple()
                .onItem().transformToMulti(tuple -> {
                    String userId = tuple.getItem1();
                    String repoId = tuple.getItem2();
                    return repository.findByUserAndRepoId(userId, repoId);
                });
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

    public Uni<List<String>> getRepositoryIds(String userId) {
        return repository.getRepositoryIds(userId)
                         .map(list -> list.stream().map(String::new).toList());
    }

    public Uni<Void> deleteUserContributions(String userId) {
        return repository.deleteByUserId(userId);
    }

    public Uni<PagedResponse<D>> search(C searchCriteria) {
        return repository.search(searchCriteria);
    }
}
