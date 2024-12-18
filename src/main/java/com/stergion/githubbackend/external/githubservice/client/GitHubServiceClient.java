package com.stergion.githubbackend.external.githubservice.client;

import com.stergion.githubbackend.external.githubservice.client.exceptions.ServiceResponseExceptionMapper;
import com.stergion.githubbackend.external.githubservice.client.routes.ContributionRoutes;
import com.stergion.githubbackend.external.githubservice.client.routes.RepositoryRoutes;
import com.stergion.githubbackend.external.githubservice.client.routes.UserRoutes;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
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
@RegisterProvider(ServiceResponseExceptionMapper.class)
@RegisterRestClient(configKey = "github-service")
public interface GitHubServiceClient
        extends UserRoutes, RepositoryRoutes, ContributionRoutes {}