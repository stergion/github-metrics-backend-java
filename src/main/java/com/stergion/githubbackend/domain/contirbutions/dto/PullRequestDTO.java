package com.stergion.githubbackend.domain.contirbutions.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record PullRequestDTO(
        @NotNull
        UserWithLogin user,

        @NotNull
        NameWithOwner repository,

        @NotNull
        Github github,

        @NotNull
        @PastOrPresent
        LocalDate createdAt,

        LocalDate mergedAt,
        LocalDate closedAt,
        LocalDate updatedAt,
        PullRequestState state,
        int reactionsCount,
        List<Label> labels,
        String title,
        String body,
        List<CommitDTO> commits,
        int commitsCount,
        int commentsCount,
        List<Github> closingIssuesReferences,
        int closingIssuesReferencesCount
) implements ContributionDTO {
    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(user, repository);
        }
    }
}
