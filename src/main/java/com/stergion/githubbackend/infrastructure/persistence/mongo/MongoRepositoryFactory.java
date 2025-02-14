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
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.MongoRepositoryRepository;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.MongoRepositoryRepositoryAdapter;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.RepositoryMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.MongoUserRepository;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.MongoUserRepositoryAdapter;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.UserMapper;
import com.stergion.githubbackend.infrastructure.persistence.utils.Database;
import com.stergion.githubbackend.infrastructure.persistence.utils.DatabaseType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Database(DatabaseType.MONGO)
public class MongoRepositoryFactory implements RepositoryFactory {

    @Inject
    MongoUserRepository userRepository;
    @Inject
    UserMapper userMapper;

    @Inject
    MongoRepositoryRepository repositoryRepository;
    @Inject
    RepositoryMapper repositoryMapper;

    @Inject
    MongoCommitRepository commitRepository;
    @Inject
    CommitMapper commitMapper;
    @Inject
    MongoCommitSearchStrategy commitSearchStrategy;

    @Inject
    MongoIssueCommentRepository issueCommentRepository;
    @Inject
    IssueCommentMapper issueCommentMapper;
    @Inject
    MongoIssueCommentSearchStrategy issueCommentSearchStrategy;

    @Inject
    MongoIssueRepository issueRepository;
    @Inject
    IssueMapper issueMapper;
    @Inject
    MongoIssueSearchStrategy issueSearchStrategy;

    @Inject
    MongoPullRequestRepository pullRequestRepository;
    @Inject
    PullRequestMapper pullRequestMapper;
    @Inject
    MongoPullRequestSearchStrategy pullRequestSearchStrategy;

    @Inject
    MongoPullRequestReviewRepository reviewRepository;
    @Inject
    PullRequestReviewMapper reviewMapper;
    @Inject
    MongoPullRequestReviewSearchStrategy reviewSearchStrategy;


    @Override
    public UserRepository createUserRepository() {
        return new MongoUserRepositoryAdapter(userRepository, userMapper);

    }


    @Override
    public RepositoryRepository createRepositoryRepository() {
        return new MongoRepositoryRepositoryAdapter(repositoryRepository, repositoryMapper);
    }

    @Override
    public CommitRepository createCommitRepository() {
        return new MongoCommitRepositoryAdapter(
                commitRepository,
                commitMapper,
                commitSearchStrategy
        );
    }

    @Override
    public IssueCommentRepository createIssueCommentRepository() {
        return new MongoIssueCommentRepositoryAdapter(
                issueCommentRepository,
                issueCommentMapper,
                issueCommentSearchStrategy
        );
    }

    @Override
    public IssueRepository createIssueRepository() {
        return new MongoIssueRepositoryAdapter(issueRepository, issueMapper, issueSearchStrategy);
    }

    @Override
    public PullRequestRepository createPullRequestRepository() {
        return new MongoPullRequestRepositoryAdapter(
                pullRequestRepository,
                pullRequestMapper,
                pullRequestSearchStrategy
        );
    }

    @Override
    public MongoPullRequestReviewRepositoryAdapter createPullRequestReviewRepository() {
        return new MongoPullRequestReviewRepositoryAdapter(
                reviewRepository,
                reviewMapper,
                reviewSearchStrategy
        );
    }

}
