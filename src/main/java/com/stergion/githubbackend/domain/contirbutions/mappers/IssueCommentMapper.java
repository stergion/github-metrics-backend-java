package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.common.mappers.ObjectIdMapper;
import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = ObjectIdMapper.class)
public interface IssueCommentMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "repository.id", source = "repositoryId")
    IssueComment toDomain(IssueCommentEntity issueComment);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "repositoryId", source = "repository.id")
    IssueCommentEntity toEntity(IssueComment issueComment);
}
