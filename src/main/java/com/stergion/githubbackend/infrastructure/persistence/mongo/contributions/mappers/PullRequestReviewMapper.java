package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utils.mapper.ObjectIdMapper;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = ObjectIdMapper.class)
public interface PullRequestReviewMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "repository.id", source = "repositoryId")
    PullRequestReview toDomain(PullRequestReviewEntity pullRequestReview);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "repositoryId", source = "repository.id")
    PullRequestReviewEntity toEntity(PullRequestReview pullRequestReview);
}
