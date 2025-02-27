package com.stergion.githubbackend.domain.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.Github;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record User(
        String id,
        @NotBlank
        String login,

        @NotBlank
        String name,

        @NotNull
        Github github,

        @Email
        String email,

        @PastOrPresent
        LocalDateTime createdAt,

        @PastOrPresent
        LocalDateTime updatedAt,

        @NotNull
        List<String> repositories,

        URI avatarURL,
        String bio,
        String twitterHandle,
        URI websiteURL
) {
    public User {
        repositories = repositories != null ? new ArrayList<>(List.copyOf(repositories)) : new ArrayList<>();
    }

    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"login\": %s, \"name\": %s}".formatted(login, name);
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof User u)
                && login.equals(u.login);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
    }
}
