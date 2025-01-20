package com.stergion.githubbackend.domain.repositories;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.utils.RepositoryIdCache;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.RepositoryClient;
import com.stergion.githubbackend.infrastructure.persistence.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.repositories.RepositoryRepository;
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
    RepositoryRepository repoRepository;

    @Inject
    RepositoryMapper repoMapper;
    @Inject
    RepositoryIdCache repositoryIdCache;

    public RepositoryDTO getRepository(String owner, String name) {
        Repository repo = repoRepository.findByNameAndOwner(owner, name);
        if (repo == null) {
            throw new RepositoryNotFoundException(owner, name);
        }
        return repoMapper.toDTO(repo);
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

    private RepositoryDTO fetchRepository(String owner, String name) {
        RepositoryDTO repo = repoClient.getRepositoryInfo(owner, name);
        if (repo == null) {
            throw new RepositoryNotFoundException(owner, name);
        }
        return repo;
    }

    private RepositoryDTO createRepository(RepositoryDTO repoDTO) {
        var repo = repoMapper.toEntity(repoDTO);
        repoRepository.persist(repo);

        return repoMapper.toDTO(repo);
    }

    private List<RepositoryDTO> createRepositories(List<RepositoryDTO> repoDTO) {
        var repos = repoDTO.stream()
                           .map(repoMapper::toEntity)
                           .distinct()
                           .toList();

        repoRepository.persist(repos);

        return repos.stream()
                    .map(repoMapper::toDTO)
                    .toList();
    }

    public RepositoryDTO fetchAndCreateRepository(NameWithOwner nameWithOwner) {
        return fetchAndCreateRepository(nameWithOwner.owner(), nameWithOwner.name());
    }

    public RepositoryDTO fetchAndCreateRepository(String owner, String name) {
        RepositoryDTO repoDTO = fetchRepository(owner, name);
        return createRepository(repoDTO);
    }

    private Multi<List<RepositoryDTO>> fetchUserRepositories(String login, LocalDateTime from,
                                                             LocalDateTime to,
                                                             BatchProcessorConfig config) {
        return repoClient.getRepositoriesContributedToBatched(login, from, to, config);
    }

    private Multi<List<RepositoryDTO>> fetchUserRepositoriesCommited(String login, LocalDateTime from,
                                                             LocalDateTime to,
                                                             BatchProcessorConfig config) {
        return repoClient.getRepositoriesCommittedToBatched(login, from, to, config);
    }

    public Multi<List<RepositoryDTO>> fetchAndCreateUserRepositories(String login, LocalDateTime from,
                                                                     LocalDateTime to) {
        var config = BatchProcessorConfig.defaultConfig();
        var repos = fetchUserRepositories(login, from, to, config);

        return repos.map(this::createRepositories);
    }

    public Multi<List<RepositoryDTO>> fetchAndCreateUserRepositoriesCommited(String login,
                                                                             LocalDateTime from,
                                                                             LocalDateTime to) {
        var config = BatchProcessorConfig.defaultConfig();
        var repos = fetchUserRepositoriesCommited(login, from, to, config);

        return repos.map(this::createRepositories);
    }

    public List<RepositoryDTO> getRepositories(List<ObjectId> ids) {
        List<Repository> repos = repoRepository.findById(ids);
        return repos.stream()
                    .map(repoMapper::toDTO)
                    .toList();
    }
}
