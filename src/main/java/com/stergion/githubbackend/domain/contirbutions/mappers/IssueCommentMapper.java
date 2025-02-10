package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IssueCommentMapper {
    @Mapping(target = "user", source = "user.login")
    IssueComment toDomain(IssueCommentEntity issueComment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "issueComment.user")
    IssueCommentEntity toEntity(IssueComment issueComment, ObjectId userId, ObjectId repositoryId);
}
