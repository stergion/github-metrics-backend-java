package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;


import com.stergion.githubbackend.common.mappers.MapStructConfig;
import com.stergion.githubbackend.domain.contirbutions.models.CommitDTO;
import com.stergion.githubbackend.domain.utils.types.CommitComment;
import com.stergion.githubbackend.domain.utils.types.File;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.CommitGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.CommitFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface CommitGHMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "login")
    @Mapping(target = "github.id", source = "commit.id")
    @Mapping(target = "github.url", source = "commit.commitUrl")
    @Mapping(target = "filesCount", expression = "java(getFilesCount(commit))")
    @Mapping(target = "commentsCount", expression = "java(getCommentsCount(commit))")
    @Mapping(target = "associatedPullRequest", source = "commit.associatedPullRequests.nodes")
    @Mapping(target = "associatedPullRequestsCount", expression = "java" +
            "(getAssociatedPullRequestsCount(commit))")
    CommitDTO toDTO(CommitGH commit, String login, NameWithOwner repository);


    default List<CommitComment> map(CommitGH.CommentsConnection comments) {
        if (comments == null || comments.nodes() == null) {
            return List.of();
        }

        return comments.nodes().stream()
                       .map(comment -> new CommitComment(
                               comment.author().login(),
                               comment.publishedAt().toString(),
                               comment.position(),
                               String.valueOf(comment.reactions().totalCount()),
                               comment.body()
                       ))
                       .toList();
    }

    default int getCommentsCount(CommitGH commit) {
        if (commit.comments() == null || commit.comments().nodes() == null) {
            return 0;
        }
        return commit.comments().nodes().size();
    }

    default int getAssociatedPullRequestsCount(CommitGH commit) {
        if (commit.associatedPullRequests() == null ||
                commit.associatedPullRequests().nodes() == null) {
            return 0;
        }
        return commit.associatedPullRequests().nodes().size();
    }

    default int getFilesCount(CommitGH commit) {
        if (commit.files() == null) {
            return 0;
        }
        return commit.files().size();
    }

    // File mapping methods
    default File map(CommitFile file) {
        if (file == null) {
            return null;
        }
        String path = file.filename();
        return new File(
                file.filename(),
                extractBaseName(path),
                extractFileExtension(path),
                path,
                file.status(),
                file.additions(),
                file.deletions(),
                file.changes(),
                file.patch()
        );
    }

    private String extractBaseName(String path) {
        if (path == null) return null;
        int lastSlash = path.lastIndexOf('/');
        String fileName = lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(0, lastDot) : fileName;
    }

    private String extractFileExtension(String path) {
        if (path == null) return null;
        int lastDot = path.lastIndexOf('.');
        return lastDot > 0 ? path.substring(lastDot + 1) : "";
    }

    default List<Github> mapAssociatedPullRequests(CommitGH commit) {
        if (commit.associatedPullRequests() == null ||
                commit.associatedPullRequests().nodes() == null) {
            return List.of();
        }
        return commit.associatedPullRequests().nodes().stream()
                       .map(pr -> new Github(pr.id(), pr.url()))
                       .toList();
    }
}
