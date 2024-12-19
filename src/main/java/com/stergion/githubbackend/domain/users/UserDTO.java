package com.stergion.githubbackend.domain.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.Github;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record UserDTO(
        @NotBlank
        String login,

        @NotBlank
        String name,

        @NotNull
        Github github,

        @Email
        String email,

        @NotNull
        List<ObjectId> repositories,

        String avatarURL,
        String bio,
        String twitterHandle,
        String websiteURL
) {
    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"name\": %s}".formatted(login, name);
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof UserDTO u)
                && login.equals(u.login);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
    }
}
