package com.stergion.githubbackend.infrastructure.persistence.postgres.users;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Mapper(config = MapStructConfig.class, imports = {LocalDateTime.class})
public interface UserMapper {
    @Mapping(target = "github.id", source = "githubId")
    @Mapping(target = "github.url", source = "githubUrl")
    @Mapping(target = "avatarURL", source = "avatarUrl")
    @Mapping(target = "websiteURL", source = "websiteUrl")
    User toDomain(UserEntity user);

    List<User> toDomain(List<UserEntity> user);

    @Mapping(target = "githubId", source = "github.id")
    @Mapping(target = "githubUrl", source = "github.url")
    @Mapping(target = "avatarUrl", source = "avatarURL")
    @Mapping(target = "websiteUrl", source = "websiteURL")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    UserEntity toEntity(User user);

    List<UserEntity> toEntity(List<User> user);
}
