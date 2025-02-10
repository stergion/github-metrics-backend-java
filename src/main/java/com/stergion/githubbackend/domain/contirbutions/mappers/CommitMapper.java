package com.stergion.githubbackend.domain.contirbutions.mappers;


import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.CommitEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface CommitMapper {
    @Mapping(target = "user", source = "user.login")
    Commit toDTO(CommitEntity commit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "commitDTO.user")
    CommitEntity toEntity(Commit commitDTO, ObjectId userId, ObjectId repositoryId);
}
