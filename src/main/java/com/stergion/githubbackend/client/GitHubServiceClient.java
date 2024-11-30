package com.stergion.githubbackend.client;

import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * Main interface for interacting with the GitHub API.
 * This interface provides access to all GitHub operations supported by the application.
 * It uses Quarkus REST Client for HTTP communication and supports Server-Sent Events (SSE) for
 * streaming responses.
 *
 * @see RegisterRestClient
 */
@Path("/api")
@RegisterRestClient(configKey = "github-service")
public interface GitHubServiceClient
        extends UserRoutes, RepositoryRoutes, ContributionRoutes {}