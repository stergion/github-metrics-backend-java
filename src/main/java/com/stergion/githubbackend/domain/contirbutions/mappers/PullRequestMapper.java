package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PullRequestMapper {
    @Mapping(target = "user", source = "user.login")
    PullRequest toDomain(PullRequestEntity pullRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "pullRequest.user")
    PullRequestEntity toEntity(PullRequest pullRequest, ObjectId userId, ObjectId repositoryId);
}
