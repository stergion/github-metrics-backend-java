package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.IssueDTO;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IssueMapper {
    @Mapping(target = "user", source = "user.login")
    IssueDTO toDTO(IssueEntity issue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "issueDTO.user")
    IssueEntity toEntity(IssueDTO issueDTO, ObjectId userId, ObjectId repositoryId);
}
