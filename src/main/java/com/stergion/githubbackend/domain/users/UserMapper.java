package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.infrastructure.persistence.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(config = MapStructConfig.class, imports = {LocalDate.class})
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "updatedAt", expression = "java(LocalDate.now())")
    User toEntity(UserDTO userDTO);
}
