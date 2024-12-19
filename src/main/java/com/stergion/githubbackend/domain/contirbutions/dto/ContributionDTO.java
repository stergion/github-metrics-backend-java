package com.stergion.githubbackend.domain.contirbutions.dto;

import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.UserWithLogin;
import jakarta.validation.constraints.NotNull;

public sealed interface ContributionDTO permits CommitDTO, IssueDTO, PullRequestDTO, PullRequestReviewDTO,
        IssueCommentDTO {
    @NotNull
    UserWithLogin user();

    @NotNull
    NameWithOwner repository();

    Github github();
}
