package com.stergion.githubbackend.infrastructure.external.githubservice.client.routes;

import io.smallrye.mutiny.Multi;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.jboss.resteasy.reactive.client.SseEvent;

import java.time.LocalDateTime;

public interface ContributionRoutes {
    /**
     * Retrieves a stream of commits made by the user to a specific repository within the
     * specified date range.
     *
     * @param login    the GitHub username
     * @param owner    the repository owner's username
     * @param name     the repository name
     * @param fromDate start date for the commits range
     * @param toDate   end date for the commits range
     * @return stream of commits
     * @throws WebApplicationException if the user or repository doesn't exist or other API
     *                                 errors occur
     */
    @GET
    @Path("/user/{login}/contributions/commits/{owner}/{name}/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getCommits(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @NotBlank(message = "Owner cannot be blank")
            @PathParam("owner") String owner,
            @NotBlank(message = "Repository name cannot be blank")
            @PathParam("name") String name,
            @PathParam("fromDate") LocalDateTime fromDate,
            @PathParam("toDate") LocalDateTime toDate
                                      );

    /**
     * Retrieves a stream of issues created by the user within the specified date range.
     *
     * @param login    the GitHub username
     * @param fromDate start date for the issues range
     * @param toDate   end date for the issues range
     * @return a stream of issues
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}/contributions/issues/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getIssues(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @PathParam("fromDate") LocalDateTime fromDate,
            @PathParam("toDate") LocalDateTime toDate
                                     );

    /**
     * Retrieves a stream of pull requests created by the user within the specified date range.
     *
     * @param login    the GitHub username
     * @param fromDate start date for the pull requests range
     * @param toDate   end date for the pull requests range
     * @return a stream of pull requests
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}/contributions/pullrequests/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getPullRequests(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @PathParam("fromDate") LocalDateTime fromDate,
            @PathParam("toDate") LocalDateTime toDate
                                           );

    /**
     * Retrieves a stream of pull request reviews created by the user within the specified date
     * range.
     *
     * @param login    the GitHub username
     * @param fromDate start date for the pull request reviews range
     * @param toDate   end date for the pull request reviews range
     * @return a stream of pull request reviews
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}/contributions/pullrequest-reviews/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getPullRequestReviews(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @PathParam("fromDate") LocalDateTime fromDate,
            @PathParam("toDate") LocalDateTime toDate
                                                 );

    /**
     * Retrieves a stream of issue comments created by the user within the specified date range.
     *
     * @param login    the GitHub username
     * @param fromDate start date for the issue comments range
     * @param toDate   end date for the issue comments range
     * @return a stream of issue comments
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}/contributions/issue-comments/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getIssueComments(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @PathParam("fromDate") LocalDateTime fromDate,
            @PathParam("toDate") LocalDateTime toDate
                                            );

    /**
     * Retrieves a stream of commit comments created by the user within the specified date range.
     *
     * @param login    the GitHub username
     * @param fromDate start date for the commit comments range
     * @param toDate   end date for the commit comments range
     * @return a stream of commit comments
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}/contributions/commit-comments/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getCommitComments(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @PathParam("fromDate") LocalDateTime fromDate,
            @PathParam("toDate") LocalDateTime toDate
                                             );
}
