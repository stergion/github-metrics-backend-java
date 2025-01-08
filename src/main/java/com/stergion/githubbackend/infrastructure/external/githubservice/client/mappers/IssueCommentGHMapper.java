package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.IssueCommentDTO;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.IssueCommentGH;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IssueCommentGHMapper {
    @Mapping(target = "user", source = "login")
    @Mapping(target = "github.id", source = "issueComment.id")
    @Mapping(target = "github.url", source = "issueComment.url")
    @Mapping(target = "repository.owner", source = "issueComment.repository.owner.login")
    @Mapping(target = "associatedIssue", expression = "java(mapAssociatedIssue(issueComment))")
    IssueCommentDTO toDTO(IssueCommentGH issueComment, String login);


    default IssueCommentDTO.AssociatedIssue mapAssociatedIssue(IssueCommentGH issueComment) {
        if (issueComment.pullRequest() != null) {
            return new IssueCommentDTO.AssociatedIssue(
                    IssueCommentDTO.IssueType.PULL_REQUEST,
                    new Github(issueComment.pullRequest().id(), issueComment.pullRequest().url())
            );
        } else if (issueComment.issue() != null) {
            return new IssueCommentDTO.AssociatedIssue(
                    IssueCommentDTO.IssueType.ISSUE,
                    new Github(issueComment.issue().id(), issueComment.issue().url())
            );
        }
        return null;
    }
}
