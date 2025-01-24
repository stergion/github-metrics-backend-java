package com.stergion.githubbackend.domain.contirbutions.search;

public interface SearchStrategyFactory {
    CommitSearchStrategy createCommitSearchStrategy();
    IssueSearchStrategy createIssueSearchStrategy();
    PullRequestSearchStrategy createPullRequestSearchStrategy();
    PullRequestReviewSearchStrategy createPullRequestReviewSearchStrategy();
    IssueCommentSearchStrategy createIssueCommentSearchStrategy();
}
