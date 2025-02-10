package com.stergion.githubbackend.application.mapper;

import com.stergion.githubbackend.application.response.NameWithOwnerResponse;
import com.stergion.githubbackend.application.response.RepositoryResponse;
import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.repositories.Repository;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface RepositoryMapper {
    RepositoryResponse toRepositoryResponse(Repository repository);

    NameWithOwnerResponse toNameWithOwnerResponse(Repository repository);
}
