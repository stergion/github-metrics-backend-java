package com.stergion.githubbackend.domain.contirbutions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IssueMapper {
    @Mapping(target = "user", source = "user.login")
    Issue toDomain(IssueEntity issue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "issue.user")
    IssueEntity toEntity(Issue issue, ObjectId userId, ObjectId repositoryId);
}
