package com.stergion.githubbackend.domain.contirbutions.mappers;


import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.CommitEntity;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface CommitMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "repository.id", source = "repositoryId")
    Commit toDomain(CommitEntity commit);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "repositoryId", source = "repository.id")
    CommitEntity toEntity(Commit commit);
}
