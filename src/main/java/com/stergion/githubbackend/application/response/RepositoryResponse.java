package com.stergion.githubbackend.application.response;

import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.domain.utils.types.Language;
import com.stergion.githubbackend.domain.utils.types.Topic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RepositoryResponse(
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

    public RepositoryResponse {
        labels = labels != null ? List.copyOf(labels) : List.of();
        languages = languages != null ? List.copyOf(languages) : List.of();
        topics = topics != null ? List.copyOf(topics) : List.of();
    }
}
