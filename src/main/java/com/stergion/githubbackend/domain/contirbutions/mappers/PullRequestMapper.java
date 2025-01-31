package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestDTO;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequest;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PullRequestMapper {
    @Mapping(target = "user", source = "user.login")
    PullRequestDTO toDTO(PullRequest pullRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "pullRequestDTO.user")
    PullRequest toEntity(PullRequestDTO pullRequestDTO, ObjectId userId, ObjectId repositoryId);
}
