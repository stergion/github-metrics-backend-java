package com.stergion.githubbackend.domain.repositories;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.utils.RepositoryIdCache;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.RepositoryClient;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.MongoRepositoryRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class RepositoryService {
    @Inject
    RepositoryClient repoClient;
    @Inject
    MongoRepositoryRepository repoRepository;

    @Inject
    RepositoryMapper repoMapper;
    @Inject
    RepositoryIdCache repositoryIdCache;

    public Repository getRepository(String owner, String name) {
        RepositoryEntity repo = repoRepository.findByNameAndOwner(owner, name);
        if (repo == null) {
            throw new RepositoryNotFoundException(owner, name);
        }
        return repoMapper.toDomain(repo);
    }

    public ObjectId getRepositoryId(NameWithOwner repo) {
        ObjectId id = repositoryIdCache.get(repo);
        if (id != null) {
            return id;
        }

        id = repoRepository.findByNameAndOwner(repo.owner(), repo.name()).id;
        if (id == null) {
            throw new RepositoryNotFoundException(repo.owner(), repo.name());
        }

        repositoryIdCache.put(repo, id);
        return id;
    }

    private Repository fetchRepository(String owner, String name) {
        Repository repo = repoClient.getRepositoryInfo(owner, name);
        if (repo == null) {
            throw new RepositoryNotFoundException(owner, name);
        }
        return repo;
    }

    private Repository createRepository(Repository repo) {
        var repoEntity = repoMapper.toEntity(repo);
        repoRepository.save(repoEntity);

        return repoMapper.toDomain(repoEntity);
    }

    private List<Repository> createRepositories(List<Repository> repo) {
        var repoEntities = repo.stream()
                           .map(repoMapper::toEntity)
                           .distinct()
                           .toList();

        repoRepository.save(repoEntities);

        return repoEntities.stream()
                    .map(repoMapper::toDomain)
                    .toList();
    }

    public Repository fetchAndCreateRepository(NameWithOwner nameWithOwner) {
        return fetchAndCreateRepository(nameWithOwner.owner(), nameWithOwner.name());
    }

    public Repository fetchAndCreateRepository(String owner, String name) {
        Repository repo = fetchRepository(owner, name);
        return createRepository(repo);
    }

    private Multi<List<Repository>> fetchUserRepositories(String login, LocalDateTime from,
                                                          LocalDateTime to,
                                                          BatchProcessorConfig config) {
        return repoClient.getRepositoriesContributedToBatched(login, from, to, config);
    }

    private Multi<List<Repository>> fetchUserRepositoriesCommited(String login, LocalDateTime from,
                                                                  LocalDateTime to,
                                                                  BatchProcessorConfig config) {
        return repoClient.getRepositoriesCommittedToBatched(login, from, to, config);
    }

    public Multi<List<Repository>> fetchAndCreateUserRepositories(String login, LocalDateTime from,
                                                                  LocalDateTime to) {
        var config = BatchProcessorConfig.defaultConfig();
        var repos = fetchUserRepositories(login, from, to, config);

        return repos.map(this::createRepositories);
    }

    public Multi<List<Repository>> fetchAndCreateUserRepositoriesCommited(String login,
                                                                          LocalDateTime from,
                                                                          LocalDateTime to) {
        var config = BatchProcessorConfig.defaultConfig();
        var repos = fetchUserRepositoriesCommited(login, from, to, config);

        return repos.map(this::createRepositories);
    }

    public List<Repository> getRepositories(List<ObjectId> ids) {
        List<RepositoryEntity> repos = repoRepository.findById(ids);
        return repos.stream()
                    .map(repoMapper::toDomain)
                    .toList();
    }
}
