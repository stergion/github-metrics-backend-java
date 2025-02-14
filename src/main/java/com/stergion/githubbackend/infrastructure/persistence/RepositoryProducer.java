package com.stergion.githubbackend.infrastructure.persistence;

import com.stergion.githubbackend.infrastructure.persistence.utils.Database;
import com.stergion.githubbackend.infrastructure.persistence.utils.DatabaseType;
import com.stergion.githubbackend.domain.contirbutions.repositories.*;
import com.stergion.githubbackend.domain.repositories.RepositoryRepository;
import com.stergion.githubbackend.domain.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RepositoryProducer {
    @ConfigProperty(name = "database.type", defaultValue = "mongo")
    String databaseType;

    @Inject
    @Any
    Instance<RepositoryFactory> factories;

    private RepositoryFactory getFactory() {
        DatabaseType type = DatabaseType.fromString(databaseType);
        return factories.select(new Database.Literal(type)).get();
    }

    @Produces
    @ApplicationScoped
    public UserRepository produceUserRepository() {
        return getFactory().createUserRepository();
    }

    @Produces
    @ApplicationScoped
    public RepositoryRepository produceRepository() {
        return getFactory().createRepositoryRepository();
    }

    @Produces
    @ApplicationScoped
    public CommitRepository produceCommitRepository() {
        return getFactory().createCommitRepository();
    }

    @Produces
    @ApplicationScoped
    public IssueCommentRepository produceIssueCommentRepository() {
        return getFactory().createIssueCommentRepository();
    }

    @Produces
    @ApplicationScoped
    public IssueRepository produceIssueRepository() {
        return getFactory().createIssueRepository();
    }

    @Produces
    @ApplicationScoped
    public PullRequestRepository producePullRequestRepository() {
        return getFactory().createPullRequestRepository();
    }

    @Produces
    @ApplicationScoped
    public PullRequestReviewRepository producePullRequestReviewRepository() {
        return getFactory().createPullRequestReviewRepository();
    }
}

