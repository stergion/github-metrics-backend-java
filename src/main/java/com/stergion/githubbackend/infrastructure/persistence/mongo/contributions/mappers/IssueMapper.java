package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utils.mapper.ObjectIdMapper;
import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = ObjectIdMapper.class)
public interface IssueMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "repository.id", source = "repositoryId")
    Issue toDomain(IssueEntity issue);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "repositoryId", source = "repository.id")
    IssueEntity toEntity(Issue issue);
}
