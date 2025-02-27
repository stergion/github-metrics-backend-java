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

public record Repository(
        String id,
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
    public Repository {
        labels = labels != null ? new ArrayList<>(List.copyOf(labels)) : new ArrayList<>();
        languages = languages != null ? new ArrayList<>(List.copyOf(languages)) : new ArrayList<>();
        topics = topics != null ? new ArrayList<>(List.copyOf(topics)) : new ArrayList<>();
    }

    private static final ObjectMapper mapper = JsonObjectMapper.create();

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
        return (o instanceof Repository r)
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
