package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.ContributionDTO;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchStrategy;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.domain.users.UserService;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.ContributionClient;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Contribution;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.ContributionRepository;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Abstract base service that provides common processing logic for all contribution types.
 * Uses the Template Method pattern to allow specific contribution services to customize
 * only what they need to.
 *
 * @param <D> The DTO type (e.g., IssueDTO, CommitDTO)
 * @param <E> The Entity type (e.g., Issue, Commit)
 */
public abstract class ContributionService<D extends ContributionDTO, E extends Contribution> {
    @Inject
    UserService userService;

    @Inject
    RepositoryService repositoryService;

    @Inject
    ContributionClient client;

    protected final ContributionRepository<E> repository;
    protected final FetchStrategy<D> fetchStrategy;

    protected ContributionService(ContributionRepository<E> repository,
                                  FetchStrategy<D> fetchStrategy) {
        this.repository = repository;
        this.fetchStrategy = fetchStrategy;
    }

    /**
     * Ensures all repositories referenced by the contributions exist in the database.
     * This is common functionality used by all contribution types.
     */
    private void ensureRepositoriesExist(List<D> batch) {
        batch.stream()
             .map(D::repository)
             .distinct()
             .forEach(repositoryService::fetchAndCreateRepository);
    }

    /**
     * Converts DTOs to entities using the appropriate mapper.
     * Uses the nameWithOwner cache to efficiently handle nameWithOwner references.
     */
    private List<E> convertToEntities(List<D> batch) {
        return batch.stream()
                    .map(dto -> mapDtoToEntity(dto,
                            userService.getUserId(dto.user()),
                            repositoryService.getRepositoryId(dto.repository())))
                    .toList();
    }

    /**
     * This is the only method that specific contribution services need to implement.
     * It defines how to map from a DTO to an entity using the appropriate mapper.
     */
    protected abstract E mapDtoToEntity(D dto, ObjectId userId, ObjectId repoId);

    protected abstract D mapEntityToDto(E entity);

    /**
     * Processes a batch of contributions:
     * 1. Ensures referenced repositories exist
     * 2. Converts DTOs to entities
     * 3. Persists entities
     * 4. Maps back to DTOs for return
     */
    protected List<D> createContributions(List<D> contributions) {
        ensureRepositoriesExist(contributions);
        List<E> contributionsE = convertToEntities(contributions);
        repository.persist(contributionsE);
        return contributionsE.stream()
                             .map(this::mapEntityToDto)
                             .toList();
    }

    /**
     * Get a single contribution by ID
     */
    public D getContribution(ObjectId id) {
        var c = repository.findById(id);
        return mapEntityToDto(c);
    }

    /**
     * Get all contributions for a user
     */
    public List<D> getUserContributions(String login) {
        ObjectId userId = userService.getUserId(login);
        return repository.findByUserId(userId).stream()
                         .map(this::mapEntityToDto)
                         .toList();
    }

    public List<D> getUserContributions(String login, String owner, String name) {
        ObjectId userId = userService.getUserId(login);
        ObjectId repoId = repositoryService.getRepositoryId(new NameWithOwner(owner, name));

        return repository.findByUserAndRepoId(userId, repoId).stream()
                         .map(this::mapEntityToDto)
                         .toList();
    }

    /**
     * Template method for fetching and creating contributions.
     * Uses the FetchStrategy to handle the fetching logic.
     */
    protected Multi<List<D>> fetchAndCreate(FetchParams params) {
        return fetchStrategy
                .fetchBatched(params, BatchProcessorConfig.defaultConfig())
                .map(this::createContributions);
    }

    public List<ObjectId> getRepositoryIds(ObjectId userId) {
        return repository.getRepositoryIds(userId);
    }

    public void deleteUserContributions(ObjectId userId) {
        repository.delete(userId);
    }
}
