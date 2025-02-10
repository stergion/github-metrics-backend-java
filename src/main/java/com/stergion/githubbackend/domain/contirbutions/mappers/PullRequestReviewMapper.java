package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestReviewEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PullRequestReviewMapper {
    @Mapping(target = "user", source = "user.login")
    PullRequestReview toDTO(PullRequestReviewEntity pullRequestReview);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "pullRequestReviewDTO.user")
    PullRequestReviewEntity toEntity(PullRequestReview pullRequestReviewDTO, ObjectId userId, ObjectId repositoryId);
}
