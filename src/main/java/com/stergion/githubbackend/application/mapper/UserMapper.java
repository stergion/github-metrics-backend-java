package com.stergion.githubbackend.application.mapper;

import com.stergion.githubbackend.application.response.UserResponse;
import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.github", target = "github")
    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "repositories", expression = "java(toNameWithOwnerList(user, repositories))")
    @Mapping(source = "user.avatarURL", target = "avatarURL")
    @Mapping(source = "user.bio", target = "bio")
    @Mapping(source = "user.twitterHandle", target = "twitterHandle")
    @Mapping(source = "user.websiteURL", target = "websiteURL")
    UserResponse toResponse(User user, List<Repository> repositories);

    @Named("toNameWithOwnerList")
    default List<NameWithOwner> toNameWithOwnerList(User user,
                                                    List<Repository> repositories) {
        if (repositories == null || repositories.isEmpty()) {
            return List.of();
        }

        // Create a map of repositories by their ID for efficient lookup
        Map<ObjectId, NameWithOwner> repoMap = repositories.stream()
                                                           .collect(Collectors.toMap(
                                                                   Repository::id,
                                                                   repo -> new NameWithOwner(
                                                                           repo.owner(),
                                                                           repo.name())));

        // Only map repositories that exist in the user's repository list
        return user.repositories().stream()
                   .map(repoMap::get)
                   .filter(Objects::nonNull)
                   .toList();
    }
}
