package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.users.UserDTO;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.UserGH;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(config = MapStructConfig.class, imports = {LocalDate.class})
public interface UserGHMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "repositories", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "github.id", source = "id")
    @Mapping(target = "github.url", source = "url")
    @Mapping(target = "avatarURL", source = "avatarUrl")
    @Mapping(target = "websiteURL", source = "websiteUrl")
    @Mapping(target = "twitterHandle", source = "twitterUsername")
    UserDTO toDTO(UserGH user);

}
