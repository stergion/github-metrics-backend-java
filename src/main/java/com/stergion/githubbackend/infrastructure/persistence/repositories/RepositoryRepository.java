package com.stergion.githubbackend.infrastructure.persistence.repositories;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

public class RepositoryRepository implements PanacheMongoRepository<Repository> {
    public Repository findByNameAndOwner(String owner, String name) {
        return find("owner = ?1 and name = ?2", owner, name).firstResult();
    }
}
