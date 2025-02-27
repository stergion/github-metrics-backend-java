package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.common.batch.BatchProcessor;
import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.models.*;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers.*;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.*;
import com.stergion.githubbackend.infrastructure.external.githubservice.utils.SseEventTransformer;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    public Multi<Commit> getCommits(String login, String owner, String name, LocalDateTime from,
                                    LocalDateTime to) {
        return client.getCommits(login, owner, name, from, to)
                     .map(event -> transformer.transform(event, CommitGH.class))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(commitGH -> commitMapper.toDomain(commitGH, login,
                             new NameWithOwner(owner, name)));
    }

    public Multi<List<Commit>> getCommitsBatched(String login, String owner, String name,
                                                 LocalDateTime from, LocalDateTime to,
                                                 BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);
        NameWithOwner repo = new NameWithOwner(owner, name);

        var stream = client.getCommits(login, owner, name, from, to)
                           .map(event -> transformer.transform(event, CommitGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(commitGH -> commitMapper.toDomain(commitGH, login, repo))
                             .toMulti();
    }

    public Multi<Issue> getIssues(String login, LocalDateTime from, LocalDateTime to) {
        return client.getIssues(login, from, to)
                     .map(event -> transformer.transform(event, IssueGH.class))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(issueGH -> issueMapper.toDomain(issueGH, login));
    }

    public Multi<List<Issue>> getIssuesBatched(String login, LocalDateTime from, LocalDateTime to,
                                               BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getIssues(login, from, to)
                           .map(event -> transformer.transform(event, IssueGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(issueGH -> issueMapper.toDomain(issueGH, login))
                             .toMulti();

    }


    public Multi<PullRequest> getPullRequests(String login, LocalDateTime from, LocalDateTime to) {
        return client.getPullRequests(login, from, to)
                     .map(event -> transformer.transform(event, PullRequestGH.class))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(pullRequestGH -> pullRequestMapper.toDomain(pullRequestGH, login));
    }

    public Multi<List<PullRequest>> getPullRequestsBatched(String login,
                                                           LocalDateTime from, LocalDateTime to,
                                                           BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getPullRequests(login, from, to)
                           .map(event -> transformer.transform(event, PullRequestGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(
                                     pullRequestGH -> pullRequestMapper.toDomain(pullRequestGH, login))
                             .toMulti();
    }

    public Multi<PullRequestReview> getPullRequestReviews(String login, LocalDateTime from,
                                                          LocalDateTime to) {
        return client.getPullRequestReviews(login, from, to)
                     .map(event -> transformer.transform(event, PullRequestReviewGH.class))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(reviewGH -> pullRequestReviewMapper.toDomain(reviewGH, login));
    }

    public Multi<List<PullRequestReview>> getPullRequestReviewsBatched(String login,
                                                                       LocalDateTime from,
                                                                       LocalDateTime to,
                                                                       BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getPullRequestReviews(login, from, to)
                           .map(event -> transformer.transform(event, PullRequestReviewGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(
                                     reviewGH -> pullRequestReviewMapper.toDomain(reviewGH, login))
                             .toMulti();
    }

    public Multi<IssueComment> getIssueComments(String login, LocalDateTime from, LocalDateTime to) {
        return client.getIssueComments(login, from, to)
                     .map(event -> transformer.transform(event, IssueCommentGH.class))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(commentGH -> issueCommentMapper.toDomain(commentGH, login));
    }

    public Multi<List<IssueComment>> getIssueCommentsBatched(String login,
                                                             LocalDateTime from, LocalDateTime to,
                                                             BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getIssueComments(login, from, to)
                           .map(event -> transformer.transform(event, IssueCommentGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(commentGH -> issueCommentMapper.toDomain(commentGH, login))
                             .toMulti();
    }
}
