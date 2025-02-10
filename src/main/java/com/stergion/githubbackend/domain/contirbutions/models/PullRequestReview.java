package com.stergion.githubbackend.domain.contirbutions.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public record PullRequestReview(
        ObjectId id,
        @NotNull
        String user,

        @NotNull
        NameWithOwner repository,

        @NotNull
        Github github,

        @NotNull
        Github pullRequest,

        @NotNull
        @PastOrPresent
        LocalDate createdAt,

        LocalDate submittedAt,
        LocalDate updatedAt,
        LocalDate publishedAt,
        LocalDate lastEditedAt,
        PullRequestReviewState state,
        String body,
        List<PullRequestReviewComment> comments
) implements Contribution {

    public PullRequestReview {
        comments = comments != null ? List.copyOf(comments) : List.of();
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
