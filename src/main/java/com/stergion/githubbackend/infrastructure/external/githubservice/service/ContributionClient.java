package com.stergion.githubbackend.infrastructure.external.githubservice.service;

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
                     .map(commitGH -> commitMapper.toDTO(commitGH, login,
                             new NameWithOwner(owner, name)));
    }

    public Multi<IssueDTO> getIssues(String login, LocalDate from, LocalDate to) {
        return client.getIssues(login, from, to)
                     .map(event -> transformer.transform(event, IssueGH.class))
                     .map(issueGH -> issueMapper.toDTO(issueGH, login));
    }

    public Multi<PullRequestDTO> getPullRequests(String login, LocalDate from, LocalDate to) {
        return client.getPullRequests(login, from, to)
                     .map(event -> transformer.transform(event, PullRequestGH.class))
                     .map(pullRequestGH -> pullRequestMapper.toDTO(pullRequestGH, login));
    }

    public Multi<PullRequestReviewDTO> getPullRequestReviews(String login, LocalDate from,
                                                             LocalDate to) {
        return client.getPullRequestReviews(login, from, to)
                     .map(event -> transformer.transform(event, PullRequestReviewGH.class))
                     .map(reviewGH -> pullRequestReviewMapper.toDTO(reviewGH, login));
    }

    public Multi<IssueCommentDTO> getIssueComments(String login, LocalDate from, LocalDate to) {
        return client.getIssueComments(login, from, to)
                     .map(event -> transformer.transform(event, IssueCommentGH.class))
                .map(commentGH -> issueCommentMapper.toDTO(commentGH, login));
    }

//    public Multi<CommitCommentGH> getCommitComments(String login, LocalDate from, LocalDate to) {
//        return client.getCommitComments(login, from, to)
//                     .onItem()
//                     .transform(event -> transformer.transform(event, CommitCommentGH.class));
//    }
}
