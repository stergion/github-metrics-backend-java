package com.stergion.githubbackend.infrastructure.persistence.postgres.repositories;

import com.stergion.githubbackend.common.Database;
import com.stergion.githubbackend.common.DatabaseType;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class RepositoryRepository implements PanacheRepositoryBase<Repository, UUID> {
}
