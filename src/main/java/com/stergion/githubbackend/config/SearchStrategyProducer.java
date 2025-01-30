package com.stergion.githubbackend.config;

import com.stergion.githubbackend.common.Database;
import com.stergion.githubbackend.common.DatabaseType;
import com.stergion.githubbackend.domain.contirbutions.search.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SearchStrategyProducer {
    @ConfigProperty(name = "database.type", defaultValue = "mongo")
    String databaseType;

    @Inject
    @Any
    Instance<SearchStrategyFactory> factories;

    private SearchStrategyFactory getFactory() {
        DatabaseType type = DatabaseType.fromString(databaseType);
        return factories.select(new Database.Literal(type)).get();
    }

    @Produces
    @ApplicationScoped
    public CommitSearchStrategy produceCommitStrategy() {
        return getFactory().createCommitSearchStrategy();
    }

    @Produces
    @ApplicationScoped
    public IssueSearchStrategy produceIssueSearchStrategy() {
        return getFactory().createIssueSearchStrategy();
    }

    @Produces
    @ApplicationScoped
    public PullRequestSearchStrategy producePullRequestSearchStrategy() {
        return getFactory().createPullRequestSearchStrategy();
    }

    @Produces
    @ApplicationScoped
    public PullRequestReviewSearchStrategy producePullRequestReviewSearchStrategy() {
        return getFactory().createPullRequestReviewSearchStrategy();
    }

    @Produces
    @ApplicationScoped
    public IssueCommentSearchStrategy produceIssueCommentSearchStrategy() {
        return getFactory().createIssueCommentSearchStrategy();
    }
}

