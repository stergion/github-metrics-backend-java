package com.stergion.githubbackend.infrastructure.persistence.mongo.repositories;

import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.repositories.RepositoryMapper;
import com.stergion.githubbackend.domain.repositories.RepositoryRepository;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class MongoRepositoryRepositoryAdapter implements RepositoryRepository {
    @Inject
    MongoRepositoryRepository repository;

    @Inject
    RepositoryMapper mapper;

    @Override
    public void persist(Repository repository) {
        RepositoryEntity entity = mapper.toEntity(repository);
        this.repository.save(entity);
    }

    @Override
    public void persist(List<Repository> repository) {
        var repositories = repository.stream()
                                     .map(mapper::toEntity)
                                     .toList();
        this.repository.save(repositories);
    }

    @Override
    public void update(Repository repository) {
        this.repository.update(mapper.toEntity(repository));
    }

    @Override
    public void update(List<Repository> repositories) {
        this.repository.update(repositories.stream()
                                           .map(mapper::toEntity)
                                           .toList());
    }

    @Override
    public void delete(Repository repository) {
        this.repository.delete(mapper.toEntity(repository));
    }

    @Override
    public void deleteByOwner(String owner) {
        repository.deleteByOwner(owner);
    }

    @Override
    public List<Repository> findById(List<String> ids) {
        var objectIds = ids.stream()
                           .map(ObjectId::new)
                           .toList();

        return repository.findById(objectIds)
                         .stream()
                         .map(mapper::toDomain)
                         .toList();
    }

    @Override
    public List<Repository> findByOwner(String owner) {
        return repository.findByOwner(owner)
                         .stream()
                         .map(mapper::toDomain)
                         .toList();
    }

    @Override
    public Repository findByNameAndOwner(String owner, String name) {
        var entity = repository.findByNameAndOwner(owner, name);
        return mapper.toDomain(entity);
    }

    @Override
    public List<Repository> findByNameAndOwner(List<NameWithOwner> nameWithOwners) {
        var entities = nameWithOwners.stream()
                                     .map(v -> mapper.toNameWithOwnerEntity(v.owner(), v.name()))
                                     .toList();

        return repository.findByNameAndOwners(entities)
                         .stream()
                         .map(mapper::toDomain)
                         .toList();
    }

    @Override
    public Repository findByGitHubId(String id) {
        var entity = repository.findByGitHubId(id);
        return mapper.toDomain(entity);
    }
}
