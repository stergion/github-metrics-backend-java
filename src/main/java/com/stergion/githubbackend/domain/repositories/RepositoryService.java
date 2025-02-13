package com.stergion.githubbackend.domain.repositories;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.utils.RepositoryIdCache;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.RepositoryClient;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.MongoRepositoryRepositoryAdapter;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryMapper;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class RepositoryService {
    @Inject
    RepositoryClient repoClient;
    @Inject
    MongoRepositoryRepositoryAdapter repoRepository;

    @Inject
    RepositoryMapper repoMapper;

    @Inject
    RepositoryIdCache repositoryIdCache;

    public Repository getRepository(String owner, String name) {
        Repository repo = repoRepository.findByNameAndOwner(owner, name);
        if (repo == null) {
            throw new RepositoryNotFoundException(owner, name);
        }
        return repo;
    }

    public String getRepositoryId(NameWithOwner repo) {
        String id = repositoryIdCache.get(repo);
        if (id != null) {
            return id;
        }

        var repository = repoRepository.findByNameAndOwner(repo.owner(), repo.name());
        if (repository == null) {
            throw new RepositoryNotFoundException(repo.owner(), repo.name());
        }

        repositoryIdCache.put(repo, repository.id());
        return repository.id();
    }

    private Repository fetchRepository(String owner, String name) {
        Repository repo = repoClient.getRepositoryInfo(owner, name);
        if (repo == null) {
            throw new RepositoryNotFoundException(owner, name);
        }
        return repo;
    }

    private Repository createRepository(Repository repo) {
        repoRepository.persist(repo);
        return repo;
    }

    private List<Repository> createRepositories(List<Repository> repo) {
        repoRepository.persist(repo);

        return repo;
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

    public List<Repository> getRepositories(List<String> ids) {
        return repoRepository.findById(ids);
    }
}
