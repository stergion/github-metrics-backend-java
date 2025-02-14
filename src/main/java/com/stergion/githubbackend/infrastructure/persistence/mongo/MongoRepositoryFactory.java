package com.stergion.githubbackend.infrastructure.persistence.mongo;

import com.stergion.githubbackend.infrastructure.persistence.utils.Database;
import com.stergion.githubbackend.infrastructure.persistence.utils.DatabaseType;
import com.stergion.githubbackend.infrastructure.persistence.RepositoryFactory;
import com.stergion.githubbackend.domain.contirbutions.repositories.CommitRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueCommentRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestRepository;
import com.stergion.githubbackend.domain.repositories.RepositoryRepository;
import com.stergion.githubbackend.domain.users.UserRepository;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.*;
import com.stergion.githubbackend.infrastructure.persistence.mongo.repositories.MongoRepositoryRepositoryAdapter;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.MongoUserRepositoryAdapter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Database(DatabaseType.MONGO)
public class MongoRepositoryFactory implements RepositoryFactory {

    @Override
    public UserRepository createUserRepository() {
        return new MongoUserRepositoryAdapter();
    }

    @Override
    public RepositoryRepository createRepositoryRepository() {
    return new MongoRepositoryRepositoryAdapter();
    }

    @Override
    public CommitRepository createCommitRepository() {
        return new MongoCommitRepositoryAdapter();
    }

    @Override
    public IssueCommentRepository createIssueCommentRepository() {
        return new MongoIssueCommentRepositoryAdapter();
    }

    @Override
    public IssueRepository createIssueRepository() {
        return new MongoIssueRepositoryAdapter();
    }

    @Override
    public PullRequestRepository createPullRequestRepository() {
        return new MongoPullRequestRepositoryAdapter();
    }

    @Override
    public MongoPullRequestReviewRepositoryAdapter createPullRequestReviewRepository() {
        return new MongoPullRequestReviewRepositoryAdapter();
    }

}
