package com.stergion.githubbackend.infrastructure.persistence;

import com.stergion.githubbackend.domain.contirbutions.repositories.*;
import com.stergion.githubbackend.domain.repositories.RepositoryRepository;
import com.stergion.githubbackend.domain.users.UserRepository;

public interface RepositoryFactory {

    UserRepository createUserRepository();

    RepositoryRepository createRepositoryRepository();

    CommitRepository createCommitRepository();

    IssueCommentRepository createIssueCommentRepository();

    IssueRepository createIssueRepository();

    PullRequestRepository createPullRequestRepository();

    PullRequestReviewRepository createPullRequestReviewRepository();
}
