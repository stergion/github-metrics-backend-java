package com.stergion.githubbackend.infrastructure.persistence.postgres.users;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;


@Mapper(config = MapStructConfig.class, imports = {LocalDateTime.class})
public interface UserMapper {
    @Mapping(target = "github.id", source = "githubId")
    @Mapping(target = "github.url", source = "githubUrl")
    @Mapping(target = "avatarURL", source = "avatarUrl")
    @Mapping(target = "websiteURL", source = "websiteUrl")
    User toDomain(UserEntity user);

    @Mapping(target = "githubId", source = "github.id")
    @Mapping(target = "githubUrl", source = "github.url")
    @Mapping(target = "avatarUrl", source = "avatarURL")
    @Mapping(target = "websiteUrl", source = "websiteURL")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    UserEntity toEntity(User user);
}
