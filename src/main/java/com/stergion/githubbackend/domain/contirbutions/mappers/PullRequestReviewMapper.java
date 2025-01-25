package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestReviewDTO;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.PullRequestReview;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface PullRequestReviewMapper {
    @Mapping(target = "user", source = "user.login")
    PullRequestReviewDTO toDTO(PullRequestReview pullRequestReview);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "pullRequestReviewDTO.user")
    PullRequestReview toEntity(PullRequestReviewDTO pullRequestReviewDTO, ObjectId userId, ObjectId repositoryId);
}
