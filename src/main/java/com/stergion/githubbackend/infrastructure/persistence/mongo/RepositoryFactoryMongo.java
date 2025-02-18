package com.stergion.githubbackend.infrastructure.persistence.mongo;

import com.stergion.githubbackend.domain.contirbutions.repositories.CommitRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueCommentRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestRepository;
import com.stergion.githubbackend.domain.repositories.RepositoryRepository;
import com.stergion.githubbackend.domain.users.UserRepository;
import com.stergion.githubbackend.infrastructure.persistence.RepositoryFactory;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.*;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.*;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search.*;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryRepositoryMongo;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryRepositoryAdapterMongo;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.UserRepositoryMongo;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.UserRepositoryAdapterMongo;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.UserMapper;
import com.stergion.githubbackend.infrastructure.persistence.utils.Database;
import com.stergion.githubbackend.infrastructure.persistence.utils.DatabaseType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Database(DatabaseType.MONGO)
public class RepositoryFactoryMongo implements RepositoryFactory {

    @Inject
    UserRepositoryMongo userRepository;
    @Inject
    UserMapper userMapper;

    @Inject
    RepositoryRepositoryMongo repositoryRepository;
    @Inject
    RepositoryMapper repositoryMapper;

    @Inject
    CommitRepositoryMongo commitRepository;
    @Inject
    CommitMapper commitMapper;
    @Inject
    CommitSearchStrategyMongo commitSearchStrategy;

    @Inject
    IssueCommentRepositoryMongo issueCommentRepository;
    @Inject
    IssueCommentMapper issueCommentMapper;
    @Inject
    IssueCommentSearchStrategyMongo issueCommentSearchStrategy;

    @Inject
    IssueRepositoryMongo issueRepository;
    @Inject
    IssueMapper issueMapper;
    @Inject
    IssueSearchStrategyMongo issueSearchStrategy;

    @Inject
    PullRequestRepositoryMongo pullRequestRepository;
    @Inject
    PullRequestMapper pullRequestMapper;
    @Inject
    PullRequestSearchStrategyMongo pullRequestSearchStrategy;

    @Inject
    PullRequestReviewRepositoryMongo reviewRepository;
    @Inject
    PullRequestReviewMapper reviewMapper;
    @Inject
    PullRequestReviewSearchStrategyMongo reviewSearchStrategy;


    @Override
    public UserRepository createUserRepository() {
        return new UserRepositoryAdapterMongo(userRepository, userMapper);

    }


    @Override
    public RepositoryRepository createRepositoryRepository() {
        return new RepositoryRepositoryAdapterMongo(repositoryRepository, repositoryMapper);
    }

    @Override
    public CommitRepository createCommitRepository() {
        return new CommitRepositoryAdapterMongo(
                commitRepository,
                commitMapper,
                commitSearchStrategy
        );
    }

    @Override
    public IssueCommentRepository createIssueCommentRepository() {
        return new IssueCommentRepositoryAdapterMongo(
                issueCommentRepository,
                issueCommentMapper,
                issueCommentSearchStrategy
        );
    }

    @Override
    public IssueRepository createIssueRepository() {
        return new IssueRepositoryAdapterMongo(issueRepository, issueMapper, issueSearchStrategy);
    }

    @Override
    public PullRequestRepository createPullRequestRepository() {
        return new PullRequestRepositoryAdapterMongo(
                pullRequestRepository,
                pullRequestMapper,
                pullRequestSearchStrategy
        );
    }

    @Override
    public PullRequestReviewRepositoryAdapterMongo createPullRequestReviewRepository() {
        return new PullRequestReviewRepositoryAdapterMongo(
                reviewRepository,
                reviewMapper,
                reviewSearchStrategy
        );
    }

}
