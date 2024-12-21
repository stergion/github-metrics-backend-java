package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.infrastructure.persistence.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "cdi", imports = {LocalDate.class})
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDate.now())")
    User toEntity(UserDTO userDTO);
}
