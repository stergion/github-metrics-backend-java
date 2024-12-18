package com.stergion.githubbackend.external.githubservice.service;

import com.stergion.githubbackend.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.external.githubservice.client.models.success.*;
import com.stergion.githubbackend.external.githubservice.utils.SseEventTransformer;
import com.stergion.githubbackend.external.githubservice.client.models.*;
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

    public Multi<Commit> getCommits(String login, String owner, String name, LocalDate from,
                                    LocalDate to) {
        return client.getCommits(login, owner, name, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, Commit.class));
    }

    public Multi<Issue> getIssues(String login, LocalDate from, LocalDate to) {
        return client.getIssues(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, Issue.class));
    }

    public Multi<PullRequest> getPullRequests(String login, LocalDate from, LocalDate to) {
        return client.getPullRequests(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, PullRequest.class));
    }

    public Multi<PullRequestReview> getPullRequestReviews(String login, LocalDate from,
                                                          LocalDate to) {
        return client.getPullRequestReviews(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, PullRequestReview.class));
    }

    public Multi<IssueComment> getIssueComments(String login, LocalDate from, LocalDate to) {
        return client.getIssueComments(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, IssueComment.class));
    }

    public Multi<CommitComment> getCommitComments(String login, LocalDate from, LocalDate to) {
        return client.getCommitComments(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, CommitComment.class));
    }
}
