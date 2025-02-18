package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(config = MapStructConfig.class)
public interface PullRequestReviewMapper {
    @Mapping(target = "github.id", source = "githubId")
    @Mapping(target = "github.url", source = "githubUrl")
    PullRequestReview toDomain(PullRequestReviewEntity commit);

    List<PullRequestReview> toDomain(List<PullRequestReviewEntity> commits);

    @Mapping(target = "githubId", source = "github.id")
    @Mapping(target = "githubUrl", source = "github.url")
    PullRequestReviewEntity toEntity(PullRequestReview commit);

    List<PullRequestReviewEntity> toEntity(List<PullRequestReview> commits);
}
