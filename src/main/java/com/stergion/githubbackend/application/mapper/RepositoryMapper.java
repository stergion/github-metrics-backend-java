package com.stergion.githubbackend.application.mapper;

import com.stergion.githubbackend.application.response.NameWithOwnerResponse;
import com.stergion.githubbackend.application.response.RepositoryResponse;
import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.repositories.RepositoryDTO;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface RepositoryMapper {
    RepositoryResponse toRepositoryResponse(RepositoryDTO repository);

    NameWithOwnerResponse toNameWithOwnerResponse(RepositoryDTO repository);
}
