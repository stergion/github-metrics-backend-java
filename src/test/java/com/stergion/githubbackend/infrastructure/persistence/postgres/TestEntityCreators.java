package com.stergion.githubbackend.infrastructure.persistence.postgres;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.*;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.User;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.*;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueState;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueType;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestReviewState;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestState;

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

    public static Commit createCommit(User user, Repository repository, String suffix) {
        Commit commit = new Commit();
        commit.setUser(user);
        commit.setRepository(repository);
        commit.setCommittedDate(LocalDateTime.now());
        commit.setPushedDate(LocalDateTime.now());
        commit.setAdditions(10);
        commit.setDeletions(5);

        commit.setGithubId(suffix);
        commit.setGithubUrl(URI.create("https://github.com/test/commit/" + suffix));
        return commit;
    }

    public static List<Commit> createCommits(User user, Repository repository, int count) {
        List<Commit> commits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            commits.add(createCommit(user, repository, "commit-" + i));
        }
        return commits;
    }

    public static CommitComment createCommitComment() {
        return new CommitComment(
                "testAuthor",
                LocalDateTime.now(),
                1,
                5,
                "Test comment body"
        );
    }

    public static List<CommitComment> createCommitComments(int count) {
        List<CommitComment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(createCommitComment());
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

    public static File createFile() {
        return createFile("");
    }

    public static File createFile(String suffix) {
        File file = new File();
        file.setFileName("file" + suffix + ".txt");
        file.setBaseName("file" + suffix);
        file.setExtension("txt");
        file.setPath("/path/to/file" + suffix);
        file.setStatus("modified");
        file.setAdditions(10);
        file.setDeletions(5);
        file.setChanges(15);
        file.setPatch("@@ -1,3 +1,3 @@\n-old\n+new");
        return file;
    }

    public static List<File> createFiles(int count) {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(createFile(Integer.toString(i)));
        }
        return files;
    }

    public static Issue createIssue(User user, Repository repository, String suffix) {
        Issue issue = new Issue();
        issue.setUser(user);
        issue.setRepository(repository);
        issue.setBody("Test issue body");
        issue.setState(IssueState.OPEN);
        issue.setCreatedAt(LocalDateTime.now());
        issue.setReactionsCount(0);

        issue.setGithubId("test-issue-" + suffix);
        issue.setGithubUrl(URI.create("https://github.com/test/issue/" + suffix));
        issue.setTitle("Test Issue " + suffix);
        return issue;
    }

    public static List<Issue> createIssues(User user, Repository repository, int count) {
        List<Issue> issues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            issues.add(createIssue(user, repository, "issue-" + i));
        }
        return issues;
    }

    public static IssueComment createIssueComment(User user, Repository repository, String suffix) {
        IssueComment comment = new IssueComment();
        comment.setUser(user);
        comment.setRepository(repository);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPublishedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setBody("Test comment body " + suffix);

        comment.setGithubId("test-comment-" + suffix);
        comment.setGithubUrl(URI.create("https://github.com/test/comment/" + suffix));

        AssociatedIssue associatedIssue = new AssociatedIssue(
                IssueType.ISSUE,
                "test-issue-" + suffix,
                "https://github.com/test/issue/" + suffix
        );
        comment.setAssociatedIssue(associatedIssue);

        return comment;
    }

    public static List<IssueComment> createIssueComments(User user, Repository repository, int count) {
        List<IssueComment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(createIssueComment(user, repository, "comment-" + i));
        }
        return comments;
    }

    public static PullRequest createPullRequest(User user, Repository repository, String suffix) {
        PullRequest pr = new PullRequest();
        pr.setUser(user);
        pr.setRepository(repository);
        pr.setCreatedAt(LocalDateTime.now());
        pr.setState(PullRequestState.OPEN);
        pr.setReactionsCount(0);
        pr.setTitle("Test Pull Request " + suffix);
        pr.setBody("Test pull request body");
        pr.setCommitsCount(0);
        pr.setCommentsCount(0);
        pr.setClosingIssuesReferencesCount(0);

        pr.setGithubId("test-pr-" + suffix);
        pr.setGithubUrl(URI.create("https://github.com/test/pr/" + suffix));
        return pr;
    }

    public static List<PullRequest> createPullRequests(User user, Repository repository, int count) {
        List<PullRequest> pullRequests = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            pullRequests.add(createPullRequest(user, repository, "pr-" + i));
        }
        return pullRequests;
    }

    public static List<PullRequestReviewComment> createPullRequestReviewComments(int count) {
        List<PullRequestReviewComment> comments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comments.add(new PullRequestReviewComment(
                    "testReviewer" + i,
                    "test-comment-id-" + i,
                    "https://github.com/test/comment/" + i,
                    "Test review comment body " + i
            ));
        }
        return comments;
    }

    public static PullRequestReview createPullRequestReview(User user, Repository repository, String suffix) {
        PullRequestReview review = new PullRequestReview();
        review.setUser(user);
        review.setRepository(repository);
        review.setPullRequest(createPullRequestRef());
        review.setCreatedAt(LocalDateTime.now());
        review.setState(PullRequestReviewState.PENDING);
        review.setBody("Test review body");

        review.setGithubId("test-review-" + suffix);
        review.setGithubUrl(URI.create("https://github.com/test/review/" + suffix));

        return review;
    }

    public static List<PullRequestReview> createPullRequestReviews(User user, Repository repository, int count) {
        List<PullRequestReview> reviews = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            reviews.add(createPullRequestReview(user, repository, "review-" + i));
        }
        return reviews;
    }

    public static PullRequestRef createPullRequestRef() {
        return new PullRequestRef(
                "test-pr-id",
                "https://github.com/test/pr"
        );
    }

    public static PullRequestReviewComment createPullRequestReviewComment() {
        return new PullRequestReviewComment(
                "testReviewer",
                "test-comment-id",
                "https://github.com/test/comment",
                "Test review comment body"
        );
    }

    public static PullRequestCommit createPullRequestCommit() {
        PullRequestCommit commit = new PullRequestCommit();
        commit.setGithubId("test-commit-id");
        commit.setGithubUrl(URI.create("https://github.com/test/commit"));
        commit.setAdditions(10);
        commit.setDeletions(5);
        commit.setChangedFiles(3);
        return commit;
    }

    public static List<PullRequestCommit> createPullRequestCommits(int count) {
        List<PullRequestCommit> commits = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PullRequestCommit commit = new PullRequestCommit();
            commit.setGithubId("test-commit-" + i);
            commit.setGithubUrl(URI.create("https://github.com/test/commit/" + i));
            commit.setAdditions(10 + i);
            commit.setDeletions(5 + i);
            commit.setChangedFiles(3 + i);
            commits.add(commit);
        }
        return commits;
    }

    public static ClosingIssuesReference createClosingIssuesReference() {
        return new ClosingIssuesReference(
                "test-issue-id",
                URI.create("https://github.com/test/issue")
        );
    }

    public static List<ClosingIssuesReference> createClosingIssuesReferences(int count) {
        List<ClosingIssuesReference> references = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            references.add(new ClosingIssuesReference(
                    "test-issue-" + i,
                    URI.create("https://github.com/test/issue/" + i)
            ));
        }
        return references;
    }

    public static Label createLabel(String name) {
        Label label = new Label();
        label.setLabel(name);
        label.setDescription("Description for " + name);
        return label;
    }

    public static List<Label> createLabels(int count) {
        List<Label> labels = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            labels.add(createLabel("label-" + i));
        }
        return labels;
    }
}
