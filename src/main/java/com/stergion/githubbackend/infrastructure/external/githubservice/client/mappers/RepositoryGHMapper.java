package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.repositories.RepositoryDTO;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.domain.utils.types.Language;
import com.stergion.githubbackend.domain.utils.types.Topic;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.RepositoryGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.LabelsConnection;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;

@Mapper(config = MapStructConfig.class)
public interface RepositoryGHMapper {
    @Mapping(target = "github.id", source = "id")
    @Mapping(target = "github.url", source = "url")
    @Mapping(target = "owner", source = "owner.login")
    @Mapping(target = "topics", source = "repositoryTopics")
    @Mapping(target = "topicsCount", source = "repositoryTopics.totalCount")
    @Mapping(target = "labelsCount", source = "labels.totalCount")
    @Mapping(target = "watcherCount", source = "watchers.totalCount")
    @Mapping(target = "languagesSize", source = "languages.totalSize")
    @Mapping(target = "languagesCount", source = "languages.totalCount")
    RepositoryDTO toDTO(RepositoryGH repository);


    default List<Topic> map(RepositoryGH.RepositoryTopicsConnection topicsConnection) {
        if (topicsConnection == null || topicsConnection.nodes() == null) {
            return List.of();
        }
        return topicsConnection.nodes().stream()
                               .filter(topic -> topic != null && topic.topic() != null)
                               .map(topic -> new Topic(topic.topic().name()))
                               .toList();
    }

    default List<Label> map(LabelsConnection labelsConnection) {
        if (labelsConnection == null || labelsConnection.nodes() == null) {
            return List.of();
        }
        return labelsConnection.nodes().stream()
                               .filter(Objects::nonNull)
                               .map(node -> new Label(node.name(), node.description()))
                               .toList();
    }

    default String map(RepositoryGH.LanguageInfo languageInfo) {
        if (languageInfo == null) {
            return null;
        }
        return languageInfo.name();
    }

    default List<Language> map(RepositoryGH.LanguagesConnection languagesConnection) {
        if (languagesConnection == null || languagesConnection.edges() == null) {
            return List.of();
        }

        int totalSize = languagesConnection.totalSize();
        return languagesConnection.edges().stream()
                                  .map(edge -> new Language(
                                          edge.node().name(),
                                          edge.size(),
                                          totalSize > 0 ? (float) edge.size() * 100 / totalSize : 0
                                  ))
                                  .toList();
    }
}
