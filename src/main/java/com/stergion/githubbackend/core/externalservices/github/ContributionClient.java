package com.stergion.githubbackend.core.externalservices.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.client.GitHubServiceClient;
import com.stergion.githubbackend.client.models.*;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

@ApplicationScoped
public class ContributionClient {
    @RestClient
    GitHubServiceClient client;

    static final ObjectMapper mapper = new ObjectMapper()
//            .registerModule(new JavaTimeModule())
            .configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);

    public Multi<Commit> getCommits(String login, String owner, String name, LocalDate from,
                                    LocalDate to) {
        return client.getCommits(login, owner, name, from, to)
                     .onItem().transform(Unchecked.function(commits -> {
                    try {
                        return mapper.readValue(commits, Commit.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    public Multi<Issue> getIssues(String login, LocalDate from, LocalDate to) {
        return client.getIssues(login, from, to)
                     .onItem().transform(issue -> mapper.convertValue(issue, Issue.class));
    }

    public Multi<PullRequest> getPullRequests(String login, LocalDate from, LocalDate to) {
        return client.getPullRequests(login, from, to)
                     .onItem()
                     .transform(
                             pullRequests -> mapper.convertValue(pullRequests, PullRequest.class));
    }

    public Multi<PullRequestReview> getPullRequestReviews(String login, LocalDate from,
                                                          LocalDate to) {
        return client.getPullRequestReviews(login, from, to)
                     .onItem()
                     .transform(pullRequestReviews -> mapper.convertValue(pullRequestReviews,
                             PullRequestReview.class));
    }

    public Multi<IssueComment> getIssueComments(String login, LocalDate from, LocalDate to) {
        return client.getIssueComments(login, from, to)
                     .onItem()
                     .transform(comment -> mapper.convertValue(comment, IssueComment.class));
    }

    public Multi<CommitComment> getCommitComments(String login, LocalDate from, LocalDate to) {
        return client.getCommitComments(login, from, to)
                     .onItem()
                     .transform(comment -> mapper.convertValue(comment, CommitComment.class));
    }
}
