package com.stergion.githubbackend.domain.contirbutions.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record CommitDTO(
        @NotNull
        UserWithLogin user,
        @NotNull
        NameWithOwner repository,
        @NotNull
        Github github,
        @NotNull
        @PastOrPresent
        LocalDate committedDate,
        LocalDate pushedDate,
        int additions,
        int deletions,
        List<CommitComment> comments,
        int commentsCount,
        List<Github> associatedPullRequest,
        int associatedPullRequestsCount,
        List<File> files,
        int filesCount
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
