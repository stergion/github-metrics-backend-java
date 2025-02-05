package com.stergion.githubbackend.infrastructure.persistence.postgres;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.Commit;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.User;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.AssociatedPullRequest;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.CommitComment;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TestEntityCreators {

    public static User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setName("Test User " + login);
        user.setGithubId("github-" + login);
        user.setGithubUrl(URI.create("https://github.com/" + login));
        user.setEmail(login + "@example.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    public static User createUser() {
        return createUser("testUser");
    }

    public static Repository createRepository(String name, String owner) {
        Repository repo = new Repository();
        repo.setName(name);
        repo.setOwner(owner);
        repo.setGithubId(owner + "-" + name + "-id");
        repo.setGithubUrl(URI.create("https://github.com/" + owner + "/" + name));
        return repo;
    }

    public static Repository createRepository(String name) {
        return createRepository(name, "testOwner");
    }

    public static List<Repository> createRepositories(int count) {
        List<Repository> repositories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repositories.add(createRepository("repo" + i));
        }
        return repositories;
    }

    public static List<User> createUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(createUser("testUser" + i));
        }
        return users;
    }

    public static Commit createCommit(User user, Repository repository) {
        Commit commit = new Commit();
        commit.setUser(user);
        commit.setRepository(repository);
        commit.setGithubId("test-commit-id");
        commit.setGithubUrl(URI.create("https://github.com/test/commit"));
        commit.setCommittedDate(LocalDateTime.now());
        commit.setPushedDate(LocalDateTime.now());
        commit.setAdditions(10);
        commit.setDeletions(5);
        return commit;
    }

    public static Commit createCommit(User user, Repository repository, String githubId) {
        Commit commit = createCommit(user, repository);
        commit.setGithubId(githubId);
        return commit;
    }

    public static List<Commit> createCommits(User user, Repository repository, int count) {
        List<Commit> commits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            commits.add(createCommit(user, repository, "commit-" + i));
        }
        return commits;
    }

    public static CommitComment createComment() {
        return new CommitComment(
                "testAuthor",
                LocalDateTime.now(),
                1,
                5,
                "Test comment body"
        );
    }

    public static List<CommitComment> createComments(int count) {
        List<CommitComment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(createComment());
        }
        return comments;
    }

    public static AssociatedPullRequest createAssociatedPullRequest() {
        return new AssociatedPullRequest(
                "test-pr-id",
                URI.create("https://github.com/test/pr")
        );
    }

    public static List<AssociatedPullRequest> createAssociatedPullRequests(int count) {
        List<AssociatedPullRequest> prs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            AssociatedPullRequest pr = new AssociatedPullRequest(
                    "test-pr-" + i,
                    URI.create("https://github.com/test/pr/" + i)
            );
            prs.add(pr);
        }
        return prs;
    }
}
