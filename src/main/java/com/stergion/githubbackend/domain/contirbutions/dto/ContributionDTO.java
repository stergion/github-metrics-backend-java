package com.stergion.githubbackend.domain.contirbutions.dto;

import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

public sealed interface ContributionDTO
        permits CommitDTO, IssueDTO, PullRequestDTO, PullRequestReviewDTO,
        IssueCommentDTO {
    ObjectId id();

    @NotNull
    String user();

    @NotNull
    NameWithOwner repository();

    Github github();
}
