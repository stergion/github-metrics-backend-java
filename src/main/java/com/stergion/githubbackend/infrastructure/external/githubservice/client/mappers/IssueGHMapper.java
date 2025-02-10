package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.IssueDTO;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.IssueGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.LabelsConnection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface IssueGHMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "login")
    @Mapping(target = "github.id", source = "issue.id")
    @Mapping(target = "github.url", source = "issue.url")
    @Mapping(target = "repository.owner", source = "issue.repository.owner.login")
    @Mapping(target = "closer", expression = "java(getCloserLogin(issue))")
    @Mapping(target = "reactionsCount", source = "issue.reactions.totalCount")
    IssueDTO toDTO(IssueGH issue, String login);


    default String getCloserLogin(IssueGH issue) {
        if (issue.timelineItems() == null || issue.timelineItems().nodes() == null) {
            return null;
        }
        return issue.timelineItems().nodes().stream()
                    .filter(item -> item.actor() != null)
                    .map(item -> item.actor().login())
                    .findFirst()
                    .orElse(null);
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
