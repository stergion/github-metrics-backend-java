package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.IssueCommentGH;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface IssueCommentGHMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.login", source = "login")
    @Mapping(target = "github.id", source = "issueComment.id")
    @Mapping(target = "github.url", source = "issueComment.url")
    @Mapping(target = "repository.id", ignore = true)
    @Mapping(target = "repository.owner", source = "issueComment.repository.owner.login")
    @Mapping(target = "associatedIssue", expression = "java(mapAssociatedIssue(issueComment))")
    IssueComment toDomain(IssueCommentGH issueComment, String login);


    default IssueComment.AssociatedIssue mapAssociatedIssue(IssueCommentGH issueComment) {
        if (issueComment.pullRequest() != null) {
            return new IssueComment.AssociatedIssue(
                    IssueComment.IssueType.PULL_REQUEST,
                    new Github(issueComment.pullRequest().id(), issueComment.pullRequest().url())
            );
        } else if (issueComment.issue() != null) {
            return new IssueComment.AssociatedIssue(
                    IssueComment.IssueType.ISSUE,
                    new Github(issueComment.issue().id(), issueComment.issue().url())
            );
        }
        return null;
    }
}
