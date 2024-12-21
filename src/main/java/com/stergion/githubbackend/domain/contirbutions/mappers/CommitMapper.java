package com.stergion.githubbackend.domain.contirbutions.mappers;


import com.stergion.githubbackend.domain.contirbutions.dto.CommitDTO;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Commit;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface CommitMapper {
    @Mapping(target = "user", source = "user.login")
    CommitDTO toDTO(Commit commit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.login", source = "commitDTO.user")
    Commit toEntity(CommitDTO commitDTO, ObjectId userId, ObjectId repositoryId);
}
