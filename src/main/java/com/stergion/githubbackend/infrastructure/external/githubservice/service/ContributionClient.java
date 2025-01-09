package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.common.batch.BatchProcessor;
import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.*;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers.*;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.*;
import com.stergion.githubbackend.infrastructure.external.githubservice.utils.SseEventTransformer;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ContributionClient {
    @RestClient
    GitHubServiceClient client;

    @Inject
    SseEventTransformer transformer;

    @Inject
    CommitGHMapper commitMapper;

    @Inject
    IssueGHMapper issueMapper;

    @Inject
    PullRequestGHMapper pullRequestMapper;

    @Inject
    PullRequestReviewGHMapper pullRequestReviewMapper;

    @Inject
    IssueCommentGHMapper issueCommentMapper;


    public Multi<CommitDTO> getCommits(String login, String owner, String name, LocalDate from,
                                       LocalDate to) {
        return client.getCommits(login, owner, name, from, to)
                     .map(event -> transformer.transform(event, CommitGH.class))
                     .filter(Objects::nonNull)
                     .map(commitGH -> commitMapper.toDTO(commitGH, login,
                             new NameWithOwner(owner, name)));
    }

    public Multi<List<CommitDTO>> getCommitsBatched(String login, String owner, String name,
                                                    LocalDate from, LocalDate to,
                                                    BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);
        NameWithOwner repo = new NameWithOwner(owner, name);

        var stream = client.getCommits(login, owner, name, from, to)
                           .map(event -> transformer.transform(event, CommitGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(commitGH -> commitMapper.toDTO(commitGH, login, repo))
                             .toMulti();
    }

    public Multi<IssueDTO> getIssues(String login, LocalDate from, LocalDate to) {
        return client.getIssues(login, from, to)
                     .map(event -> transformer.transform(event, IssueGH.class))
                     .filter(Objects::nonNull)
                     .map(issueGH -> issueMapper.toDTO(issueGH, login));
    }

    public Multi<List<IssueDTO>> getIssuesBatched(String login, LocalDate from, LocalDate to,
                                                  BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getIssues(login, from, to)
                           .map(event -> transformer.transform(event, IssueGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(issueGH -> issueMapper.toDTO(issueGH, login))
                             .toMulti();

    }


    public Multi<PullRequestDTO> getPullRequests(String login, LocalDate from, LocalDate to) {
        return client.getPullRequests(login, from, to)
                     .map(event -> transformer.transform(event, PullRequestGH.class))
                     .filter(Objects::nonNull)
                     .map(pullRequestGH -> pullRequestMapper.toDTO(pullRequestGH, login));
    }

    public Multi<List<PullRequestDTO>> getPullRequestsBatched(String login,
                                                              LocalDate from, LocalDate to,
                                                              BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getPullRequests(login, from, to)
                           .map(event -> transformer.transform(event, PullRequestGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(
                                     pullRequestGH -> pullRequestMapper.toDTO(pullRequestGH, login))
                             .toMulti();
    }

    public Multi<PullRequestReviewDTO> getPullRequestReviews(String login, LocalDate from,
                                                             LocalDate to) {
        return client.getPullRequestReviews(login, from, to)
                     .map(event -> transformer.transform(event, PullRequestReviewGH.class))
                     .filter(Objects::nonNull)
                     .map(reviewGH -> pullRequestReviewMapper.toDTO(reviewGH, login));
    }

    public Multi<List<PullRequestReviewDTO>> getPullRequestReviewsBatched(String login,
                                                                          LocalDate from,
                                                                          LocalDate to,
                                                                          BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getPullRequestReviews(login, from, to)
                           .map(event -> transformer.transform(event, PullRequestReviewGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(
                                     reviewGH -> pullRequestReviewMapper.toDTO(reviewGH, login))
                             .toMulti();
    }

    public Multi<IssueCommentDTO> getIssueComments(String login, LocalDate from, LocalDate to) {
        return client.getIssueComments(login, from, to)
                     .map(event -> transformer.transform(event, IssueCommentGH.class))
                     .filter(Objects::nonNull)
                     .map(commentGH -> issueCommentMapper.toDTO(commentGH, login));
    }

    public Multi<List<IssueCommentDTO>> getIssueCommentsBatched(String login,
                                                                LocalDate from, LocalDate to,
                                                                BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getIssueComments(login, from, to)
                           .map(event -> transformer.transform(event, IssueCommentGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(commentGH -> issueCommentMapper.toDTO(commentGH, login))
                             .toMulti();
    }


//    public Multi<CommitCommentGH> getCommitComments(String login, LocalDate from, LocalDate to) {
//        return client.getCommitComments(login, from, to)
//                     .onItem()
//                     .transform(event -> transformer.transform(event, CommitCommentGH.class));
//    }
}
