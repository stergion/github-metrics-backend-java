package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.infrastructure.persistence.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(config = MapStructConfig.class, imports = {LocalDateTime.class})
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    User toEntity(UserDTO userDTO);
}
