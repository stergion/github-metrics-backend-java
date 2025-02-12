package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.common.mappers.ObjectIdMapper;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = ObjectIdMapper.class)
public interface PullRequestMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "repository.id", source = "repositoryId")
    PullRequest toDomain(PullRequestEntity pullRequest);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "repositoryId", source = "repository.id")
    PullRequestEntity toEntity(PullRequest pullRequest);
}
