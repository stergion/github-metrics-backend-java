package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search;

import com.stergion.githubbackend.common.Database;
import com.stergion.githubbackend.common.DatabaseType;
import com.stergion.githubbackend.domain.contirbutions.search.*;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Database(DatabaseType.MONGO)
public class MongoSearchStrategyFactory implements SearchStrategyFactory {
    @Inject
    MongoCommitRepository commitRepository;
    @Inject
    MongoIssueRepository issueRepository;
    @Inject
    MongoPullRequestRepository pullRequestRepository;
    @Inject
    MongoPullRequestReviewRepository pullRequestReviewRepository;
    @Inject
    MongoIssueCommentRepository issueCommentRepository;

    @Override
    public CommitSearchStrategy createCommitSearchStrategy() {
        return new MongoCommitSearchStrategy(commitRepository);
    }

    @Override
    public IssueSearchStrategy createIssueSearchStrategy() {
        return new MongoIssueSearchStrategy(issueRepository);
    }

    @Override
    public PullRequestSearchStrategy createPullRequestSearchStrategy() {
        return new MongoPullRequestSearchStrategy(pullRequestRepository);
    }

    @Override
    public PullRequestReviewSearchStrategy createPullRequestReviewSearchStrategy() {
        return new MongoPullRequestReviewSearchStrategy(pullRequestReviewRepository);
    }
    
    @Override
    public IssueCommentSearchStrategy createIssueCommentSearchStrategy() {
        return new MongoIssueCommentSearchStrategy(issueCommentRepository);
    }

}
