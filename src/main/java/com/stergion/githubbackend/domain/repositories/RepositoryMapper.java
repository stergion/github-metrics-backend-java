package com.stergion.githubbackend.domain.repositories;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.common.mappers.ObjectIdMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryEntity;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class, uses = ObjectIdMapper.class)
public interface RepositoryMapper {
    Repository toDomain(RepositoryEntity repository);

    RepositoryEntity toEntity(Repository repository);

    NameWithOwner toNameWithOwnerEntity(String owner, String name);
}
