package com.stergion.githubbackend.infrastructure.persistence;

import com.stergion.githubbackend.domain.contirbutions.repositories.CommitRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueCommentRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueRepository;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestRepository;
import com.stergion.githubbackend.domain.repositories.RepositoryRepository;
import com.stergion.githubbackend.domain.users.UserRepository;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.MongoPullRequestReviewRepositoryAdapter;

public interface RepositoryFactory {

    UserRepository createUserRepository();

    RepositoryRepository createRepositoryRepository();

    CommitRepository createCommitRepository();

    IssueCommentRepository createIssueCommentRepository();

    IssueRepository createIssueRepository();

    PullRequestRepository createPullRequestRepository();

    MongoPullRequestReviewRepositoryAdapter createPullRequestReviewRepository();
}
