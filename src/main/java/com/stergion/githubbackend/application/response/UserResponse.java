package com.stergion.githubbackend.application.response;

import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;

public record UserResponse(
        @NotBlank
        String login,

        @NotBlank
        String name,

        @NotNull
        Github github,

        @Email
        String email,

        @NotNull
        List<NameWithOwner> repositories,

        URI avatarURL,
        String bio,
        String twitterHandle,
        URI websiteURL
) {
    public UserResponse {
        repositories = repositories != null ? List.copyOf(repositories) : List.of();
    }

}
