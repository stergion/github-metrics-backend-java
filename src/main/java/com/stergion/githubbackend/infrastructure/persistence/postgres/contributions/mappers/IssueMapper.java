package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(config = MapStructConfig.class)
public interface IssueMapper {
    @Mapping(target = "github.id", source = "githubId")
    @Mapping(target = "github.url", source = "githubUrl")
    Issue toDomain(IssueEntity commit);

    List<Issue> toDomain(List<IssueEntity> commits);

    @Mapping(target = "githubId", source = "github.id")
    @Mapping(target = "githubUrl", source = "github.url")
    IssueEntity toEntity(Issue commit);

    List<IssueEntity> toEntity(List<Issue> commits);
}
