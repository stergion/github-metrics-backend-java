package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
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

    public Multi<CommitGH> getCommits(String login, String owner, String name, LocalDate from,
                                      LocalDate to) {
        return client.getCommits(login, owner, name, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, CommitGH.class));
    }

    public Multi<IssueGH> getIssues(String login, LocalDate from, LocalDate to) {
        return client.getIssues(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, IssueGH.class));
    }

    public Multi<PullRequestGH> getPullRequests(String login, LocalDate from, LocalDate to) {
        return client.getPullRequests(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, PullRequestGH.class));
    }

    public Multi<PullRequestReviewGH> getPullRequestReviews(String login, LocalDate from,
                                                            LocalDate to) {
        return client.getPullRequestReviews(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, PullRequestReviewGH.class));
    }

    public Multi<IssueCommentGH> getIssueComments(String login, LocalDate from, LocalDate to) {
        return client.getIssueComments(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, IssueCommentGH.class));
    }

    public Multi<CommitCommentGH> getCommitComments(String login, LocalDate from, LocalDate to) {
        return client.getCommitComments(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, CommitCommentGH.class));
    }
}
