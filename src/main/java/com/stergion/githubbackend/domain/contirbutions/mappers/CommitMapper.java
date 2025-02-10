package com.stergion.githubbackend.domain.contirbutions.mappers;


import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.CommitDTO;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.CommitEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface CommitMapper {
    @Mapping(target = "user", source = "user.login")
    CommitDTO toDTO(CommitEntity commit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "commitDTO.user")
    CommitEntity toEntity(CommitDTO commitDTO, ObjectId userId, ObjectId repositoryId);
}
