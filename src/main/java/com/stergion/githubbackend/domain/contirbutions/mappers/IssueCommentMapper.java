package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.domain.contirbutions.dto.IssueCommentDTO;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.IssueComment;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface IssueCommentMapper {
    @Mapping(target = "user", source = "user.login")
    IssueCommentDTO toDTO(IssueComment issueComment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "issueCommentDTO.user")
    IssueComment toEntity(IssueCommentDTO issueCommentDTO, ObjectId userId, ObjectId repositoryId);
}
