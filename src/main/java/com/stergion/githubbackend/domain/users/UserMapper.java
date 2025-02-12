package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.common.mappers.ObjectIdMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(config = MapStructConfig.class, imports = {LocalDateTime.class}, uses =
        ObjectIdMapper.class)
public interface UserMapper {
    User toDomain(UserEntity user);

    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    UserEntity toEntity(User user);
}
