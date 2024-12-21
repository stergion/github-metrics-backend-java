package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.domain.contirbutions.dto.IssueDTO;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Issue;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface IssueMapper {
    @Mapping(target = "user", source = "user.login")
    IssueDTO toDTO(Issue issue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "issueDTO.user")
    Issue toEntity(IssueDTO issueDTO, ObjectId userId, ObjectId repositoryId);
}
