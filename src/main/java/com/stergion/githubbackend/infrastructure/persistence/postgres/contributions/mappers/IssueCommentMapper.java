package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueCommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(config = MapStructConfig.class)
public interface IssueCommentMapper {
    @Mapping(target = "github.id", source = "githubId")
    @Mapping(target = "github.url", source = "githubUrl")
    IssueComment toDomain(IssueCommentEntity commit);

    List<IssueComment> toDomain(List<IssueCommentEntity> commits);

    @Mapping(target = "githubId", source = "github.id")
    @Mapping(target = "githubUrl", source = "github.url")
    IssueCommentEntity toEntity(IssueComment commit);

    List<IssueCommentEntity> toEntity(List<IssueComment> commits);
}
