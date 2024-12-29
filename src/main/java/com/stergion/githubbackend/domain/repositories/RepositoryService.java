package com.stergion.githubbackend.domain.repositories;

import com.stergion.githubbackend.domain.utils.RepositoryIdCache;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.RepositoryClient;
import com.stergion.githubbackend.infrastructure.persistence.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.repositories.RepositoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

@ApplicationScoped
public class RepositoryService {
    @Inject
    RepositoryClient repoClient;
    @Inject
    RepositoryRepository repoRepository;

    @Inject
    RepositoryMapper repoMapper;

    RepositoryIdCache repositoryIdCache = new RepositoryIdCache(2000);

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

    private void createRepository(RepositoryDTO repoDTO) {
        repoRepository.persist(repoMapper.toEntity(repoDTO));
    }

    public void fetchAndCreateRepository(NameWithOwner nameWithOwner) {
        fetchAndCreateRepository(nameWithOwner.owner(), nameWithOwner.name());
    }

    public void fetchAndCreateRepository(String owner, String name) {
        try {
            getRepository(owner, name);
        } catch (RepositoryNotFoundException e) {
//          Create repository only if not found
            RepositoryDTO repoDTO = fetchRepository(owner, name);
            createRepository(repoDTO);
        }
    }
}
