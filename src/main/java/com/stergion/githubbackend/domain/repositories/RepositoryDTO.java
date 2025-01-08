package com.stergion.githubbackend.domain.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.domain.utils.types.Language;
import com.stergion.githubbackend.domain.utils.types.Topic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public record RepositoryDTO(
        @NotBlank
        String owner,

        @NotBlank
        String name,

        @NotNull
        Github github,

        List<Label> labels,
        int labelsCount,
        String primaryLanguage,
        List<Language> languages,
        int languagesCount,
        int languagesSize,
        List<Topic> topics,
        int topicsCount,
        int forkCount,
        int stargazerCount,
        int watcherCount
) {
    public RepositoryDTO {
        labels = labels != null ? List.copyOf(labels) : List.of();
        languages = languages != null ? List.copyOf(languages) : List.of();
        topics = topics != null ? List.copyOf(topics) : List.of();
    }
    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"owner\": %s, \"name\": %s}".formatted(owner, name);
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof RepositoryDTO r)
                && owner.equals(r.owner)
                && name.equals(r.name);
    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
