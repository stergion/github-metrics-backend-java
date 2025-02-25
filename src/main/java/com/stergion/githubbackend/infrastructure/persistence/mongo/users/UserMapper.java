package com.stergion.githubbackend.infrastructure.persistence.mongo.users;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utils.mapper.ObjectIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(config = MapStructConfig.class, imports = {LocalDateTime.class}, uses =
        ObjectIdMapper.class)
public interface UserMapper {
    User toDomain(UserEntity user);

    List<User> toDomain(List<UserEntity> user);

    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    UserEntity toEntity(User user);

    List<UserEntity> toEntity(List<User> user);
}
