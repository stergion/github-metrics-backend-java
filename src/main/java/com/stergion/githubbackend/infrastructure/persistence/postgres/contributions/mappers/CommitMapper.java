package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.CommitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(config = MapStructConfig.class)
public interface CommitMapper {
    @Mapping(target = "github.id", source = "githubId")
    @Mapping(target = "github.url", source = "githubUrl")
    Commit toDomain(CommitEntity commit);

    List<Commit> toDomain(List<CommitEntity> commits);

    @Mapping(target = "githubId", source = "github.id")
    @Mapping(target = "githubUrl", source = "github.url")
    CommitEntity toEntity(Commit commit);

    List<CommitEntity> toEntity(List<Commit> commits);
}
