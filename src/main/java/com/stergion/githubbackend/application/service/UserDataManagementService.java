package com.stergion.githubbackend.application.service;

import com.stergion.githubbackend.domain.contirbutions.services.*;
import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.domain.users.UserAlreadyExistsException;
import com.stergion.githubbackend.domain.users.UserService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@ApplicationScoped
public class UserDataManagementService {
    @Inject
    UserService userService;

    @Inject
    RepositoryService repositoryService;

    @Inject
    CommitService commitService;
    @Inject
    IssueService issueService;
    @Inject
    PullRequestService pullRequestService;
    @Inject
    PullRequestReviewService pullRequestReviewService;
    @Inject
    IssueCommentService issueCommentService;

    record OperationResult<T>(OperationType type, T item, boolean success, Throwable error) {}

    enum OperationType {
        COMMIT,
        ISSUE,
        PULL_REQUEST,
        PULL_REQUEST_REVIEW,
        ISSUE_COMMENT
    }

    public void setupUser(String login) {
        LocalDateTime from = LocalDateTime.now().minusMonths(3);
        LocalDateTime to = LocalDateTime.now();

        // Check if user already exists
        Log.debug("Checking if user '" + login + "' exists");
        if (userService.check(login)) {
            throw new UserAlreadyExistsException(login);
        }

        // Create new user
        Log.debug("Fetching and saving user info of '" + login + "'");
        User user = userService.fetchAndCreateUser(login);

        // Update user Repositories and Contributions
        doUpdate(user, from, to);
    }

    public void updateUser(String login) {
        User user = userService.getUser(login);

        // Update user Repositories and Contributions
        doUpdate(user, user.updatedAt(), LocalDateTime.now());

        // Update user info
        userService.fetchAndUpdateUser(user.login());
    }

    public void reactivateUser() {
        /*
         * TODO: IMPLEMENT SOFT DELETION
         *
         * Reactivate user access
         * Make user data discoverable
         * Action can be reversed by calling terminateUser()
         */
        throw new NotImplementedYet();
    }

    public void terminateUser() {
        /*
         * TODO: IMPLEMENT SOFT DELETION
         *
         * Terminate user access
         * Make user data not discoverable
         * Action can be reversed by calling reactivateUser()
         */
        throw new NotImplementedYet();
    }

    public void deleteUser(String login) {
        User user = userService.getUser(login);

        Log.debugf("Deleting user '%s' Commit contributions", user.login());
        commitService.deleteUserContributions(user.id());
        Log.debugf("Deleting user '%s' Issue contributions", user.login());
        issueService.deleteUserContributions(user.id());
        Log.debugf("Deleting user '%s' Pull Request contributions", user.login());
        pullRequestService.deleteUserContributions(user.id());
        Log.debugf("Deleting user '%s' Pull Request Review contributions", user.login());
        pullRequestReviewService.deleteUserContributions(user.id());
        Log.debugf("Deleting user '%s' Issue Comment contributions", user.login());
        issueCommentService.deleteUserContributions(user.id());

        Log.debugf("Deleting user '%s' info", user.login());
        userService.deleteUser(login);
    }

    private void doUpdate(User user, LocalDateTime from, LocalDateTime to) {
        String login = user.login();
        // Get user repositories
        Log.debug("Fetching and saving repository info of '" + user.login() + "'");
        List<Repository> repos = repositoryService.fetchAndCreateUserRepositories(login,
                                                             from, to)
                                                  .collect()
                                                  .asList()
                                                  .map(lists -> lists.stream()
                                                                        .flatMap(List::stream)
                                                                        .toList())
                                                  .await()
                                                  .indefinitely();
        Log.debugf("Number of repositories created: %d", repos.size());

        Log.debug("Updating repository references of '" + login + "'");

        user = userService.updateRepositories(user, repos);

        Log.debugf("User '%s' updated:\n%s", user.login(), user);

        // Get repositories user committed to
        Log.debug(
                "Fetching and saving repository info on committed to repositories of '" + login + "'");
        List<Repository> reposCommited =
                repositoryService.fetchAndCreateUserRepositoriesCommited(
                                         login,
                                         from,
                                         to).collect()
                                 .asList()
                                 .map(lists -> lists.stream()
                                                    .flatMap(List::stream)
                                                    .toList())
                                 .await()
                                 .indefinitely();
        Log.debugf("Number of committed repositories created: %d", repos.size());

        Log.debug("Updating repository references of '" + login + "'");
        user = userService.updateRepositories(user, reposCommited);

        Log.debugf("User '%s' updated:\n%s", user.login(), user);

        // Update Contributions
        Multi<OperationResult<Repository>> commitResult = performOperation(
                OperationType.COMMIT,
                repos,
                repo -> commitService.fetchAndCreateCommits(login,
                        repo.owner(),
                        repo.name(),
                        from, to));
        Multi<OperationResult<Repository>> issueResult = performOperation(
                OperationType.ISSUE,
                repos,
                repo -> issueService.fetchAndCreateIssues(login, from, to));

        Multi<OperationResult<Repository>> prResult = performOperation(
                OperationType.PULL_REQUEST,
                repos,
                repo -> pullRequestService.fetchAndCreatePullRequests(login, from, to));

        Multi<OperationResult<Repository>> prrResult = performOperation(
                OperationType.PULL_REQUEST_REVIEW,
                repos,
                repo -> pullRequestReviewService.fetchAndCreatePullRequestReviews(login, from, to));

        Multi<OperationResult<Repository>> issueCommentResult = performOperation(
                OperationType.ISSUE_COMMENT,
                repos,
                repo -> issueCommentService.fetchAndCreateIssueComments(login, from, to));

        Uni<Void> completion = Multi.createBy().merging().streams(commitResult, issueResult,
                                            prResult, prrResult, issueCommentResult)
                                    .invoke(op -> {
                                        Log.debugf(
                                                "Creating contributions of type %s from " +
                                                        "repository '%s/%s':\t%s",
                                                op.type,
                                                op.item.owner(),
                                                op.item.name(),
                                                op.success ? "success" : "failure");
                                        if (!op.success) {
                                            Log.debug("Error:" + op.error);
                                            Log.debug("Message: " + op.error.getMessage());
                                            Log.debug("Cause: " + op.error.getCause());
                                            Log.debug("getStackTrace: " + Arrays.toString(
                                                    op.error.getStackTrace()));
                                        }
                                    })
                                    .onFailure()
                                    .invoke(Log::error)
                                    .collect()
                                    .asList()
                                    .replaceWithVoid();


        completion.await().indefinitely();
        Log.debug("Updating repository references of '" + login + "'");
        Set<String> reposUpd = HashSet.newHashSet(1000);
        reposUpd.addAll(commitService.getRepositoryIds(user.id()).await().indefinitely());
        reposUpd.addAll(issueService.getRepositoryIds(user.id()).await().indefinitely());
        reposUpd.addAll(pullRequestService.getRepositoryIds(user.id()).await().indefinitely());
        reposUpd.addAll(
                pullRequestReviewService.getRepositoryIds(user.id()).await().indefinitely());
        reposUpd.addAll(issueCommentService.getRepositoryIds(user.id()).await().indefinitely());

        userService.updateRepositoriesFromIds(user, reposUpd.stream().toList());
    }


    private <T> Multi<OperationResult<T>> performOperation(
            OperationType type,
            List<T> items,
            Function<T, Multi<?>> operation) {
        return Multi.createFrom()
                    .iterable(items)
                    .onItem()
                    .transformToUniAndMerge(item ->
                                    operation.apply(item)
                                             .onFailure().retry().atMost(3)
                                             .collect().asList()
                                             .map(result -> new OperationResult<>(type, item,
                                                     true, null))
                                             .onFailure()
                                             .recoverWithItem(
                                                     error -> new OperationResult<>(type, item,
                                                             false,
                                                             error))
                                           );
    }

}
