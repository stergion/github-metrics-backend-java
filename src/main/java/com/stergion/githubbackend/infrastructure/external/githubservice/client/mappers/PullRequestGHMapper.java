package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.domain.utils.types.PullRequestCommit;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.PullRequestGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.LabelsConnection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface PullRequestGHMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.login", source = "login")
    @Mapping(target = "repository.id", ignore = true)
    @Mapping(target = "repository.owner", source = "pullRequest.repository.owner.login")
    @Mapping(target = "github.id", source = "pullRequest.id")
    @Mapping(target = "github.url", source = "pullRequest.url")
    @Mapping(target = "commitsCount", source = "pullRequest.commits.totalCount")
    @Mapping(target = "commentsCount", source = "pullRequest.comments.totalCount")
    @Mapping(target = "reactionsCount", source = "pullRequest.reactions.totalCount")
    @Mapping(target = "closingIssuesReferences", source = "pullRequest.closingIssuesReferences.nodes")
    @Mapping(target = "closingIssuesReferencesCount", source = "pullRequest.closingIssuesReferences.totalCount")
    PullRequest toDomain(PullRequestGH pullRequest, String login);


    default List<Github> map(PullRequestGH.ClosingIssuesReferences refs) {
        if (refs == null || refs.nodes() == null) {
            return List.of();
        }
        return refs.nodes().stream()
                   .map(node -> new Github(node.id(), node.url()))
                   .toList();
    }

    default List<PullRequestCommit> map(PullRequestGH.CommitsConnection commits) {
        if (commits == null || commits.nodes() == null) {
            return List.of();
        }

        return commits.nodes().stream()
                      .map(node -> new PullRequestCommit(
                              new Github(node.commit().id(), node.commit().commitUrl()),
                              node.commit().additions(),
                              node.commit().deletions(),
                              node.commit().changedFiles()
                      ))
                      .toList();
    }

    default List<Label> map(LabelsConnection labels) {
        if (labels == null || labels.nodes() == null) {
            return List.of();
        }

        return labels.nodes().stream()
                     .map(node -> new Label(node.name(), node.description()))
                     .toList();
    }
}
