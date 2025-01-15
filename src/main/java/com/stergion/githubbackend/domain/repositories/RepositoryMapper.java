package com.stergion.githubbackend.domain.repositories;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.infrastructure.persistence.repositories.Repository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface RepositoryMapper {
    RepositoryDTO toDTO(Repository repository);

    Repository toEntity(RepositoryDTO repositoryDTO);
}
