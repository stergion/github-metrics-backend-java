package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.ClosingIssuesReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(config = MapStructConfig.class)
public interface PullRequestMapper {
    @Mapping(target = "github.id", source = "githubId")
    @Mapping(target = "github.url", source = "githubUrl")
    PullRequest toDomain(PullRequestEntity commit);

    List<PullRequest> toDomain(List<PullRequestEntity> commits);

    @Mapping(target = "githubId", source = "github.id")
    @Mapping(target = "githubUrl", source = "github.url")
    PullRequestEntity toEntity(PullRequest commit);

    List<PullRequestEntity> toEntity(List<PullRequest> commits);

    default ClosingIssuesReference mapClosingIssuesReference(Github gh) {
        return new ClosingIssuesReference(gh.id(), gh.url());
    }
}
