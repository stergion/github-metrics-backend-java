package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.IssueCommentDTO;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IssueCommentMapper {
    @Mapping(target = "user", source = "user.login")
    IssueCommentDTO toDTO(IssueCommentEntity issueComment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "issueCommentDTO.user")
    IssueCommentEntity toEntity(IssueCommentDTO issueCommentDTO, ObjectId userId, ObjectId repositoryId);
}
