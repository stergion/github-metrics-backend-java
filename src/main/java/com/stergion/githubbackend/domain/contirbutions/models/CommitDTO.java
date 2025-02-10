package com.stergion.githubbackend.domain.contirbutions.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.CommitComment;
import com.stergion.githubbackend.domain.utils.types.File;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public record CommitDTO(
        ObjectId id,
        @NotNull
        String user,
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
    public CommitDTO {
        // Ensure lists are never null
        files = files != null ? List.copyOf(files) : List.of();
        comments = comments != null ? List.copyOf(comments) : List.of();
        associatedPullRequest = associatedPullRequest != null ? List.copyOf(
                associatedPullRequest) : List.of();
    }

    private static final ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(user, repository);
        }
    }
}
