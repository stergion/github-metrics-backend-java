package com.stergion.githubbackend.domain.contirbutions.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.domain.utils.types.UserWithLogin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record IssueCommentDTO(
        @NotNull
        UserWithLogin user,

        @NotNull
        NameWithOwner repository,

        @NotNull
        Github github,

        @NotNull
        @PastOrPresent
        LocalDate createdAt,

        LocalDate publishedAt,
        LocalDate updatedAt,
        LocalDate lastEditedAt,
        AssociatedIssue associatedIssue,
        String body
) implements ContributionDTO {
    static ObjectMapper mapper = JsonObjectMapper.create();

    public record AssociatedIssue(IssueType type, Github github) {
        @Override
        public String toString() {
            return "{type: " + type + ", github: " + github + "}";
        }
    }

    public enum IssueType {
        ISSUE, PULL_REQUEST,
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(user, repository);
        }
    }
}
