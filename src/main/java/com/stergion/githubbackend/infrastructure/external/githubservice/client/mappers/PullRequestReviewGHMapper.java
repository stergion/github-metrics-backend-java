package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestReviewDTO;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.PullRequestReviewComment;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.PullRequestReviewGH;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface PullRequestReviewGHMapper {
    @Mapping(target = "user", source = "login")
    @Mapping(target = "repository.owner", source = "pullRequestReview.repository.owner.login")
    @Mapping(target = "github.id", source = "pullRequestReview.id")
    @Mapping(target = "github.url", source = "pullRequestReview.url")
    PullRequestReviewDTO toDTO(PullRequestReviewGH pullRequestReview, String login);


    default List<PullRequestReviewComment> map(PullRequestReviewGH.CommentsConnection comments) {
        if (comments == null || comments.nodes() == null) {
            return List.of();
        }

        return comments.nodes().stream()
                       .map(node -> new PullRequestReviewComment(
                               node.author().login(),
                               new Github(node.id(), node.url()),
                               node.body()
                       ))
                       .toList();
    }
}
