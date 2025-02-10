package com.stergion.githubbackend.domain.repositories;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryEntity;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface RepositoryMapper {
    Repository toDomain(RepositoryEntity repository);

    RepositoryEntity toEntity(Repository repository);
}
