package com.stergion.githubbackend.infrastructure.external.githubservice.client.routes;

import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.RepositoryGH;
import io.smallrye.mutiny.Multi;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.jboss.resteasy.reactive.client.SseEvent;

import java.time.LocalDate;

public interface RepositoryRoutes {
    /**
     * Retrieves detailed information about a GitHub repository.
     *
     * @param owner the repository owner's username
     * @param name  the repository name
     * @return repository information
     * @throws WebApplicationException if the repository doesn't exist or other API errors occur
     */
    @GET
    @Path("/repository/{owner}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    RepositoryGH getRepository(
            @NotBlank(message = "Owner cannot be blank")
            @PathParam("owner") String owner,
            @NotBlank(message = "Repository name cannot be blank")
            @PathParam("name") String name
                              );

    /**
     * Retrieves a stream of repositories that the user has contributed to within the specified
     * date range.
     *
     * @param login    the GitHub username
     * @param fromDate start date for the contribution range
     * @param toDate   end date for the contribution range
     * @return a stream of repository information
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}/repositories/contributed-to/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getRepositoriesContributedTo(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @PathParam("fromDate") LocalDate fromDate,
            @PathParam("toDate") LocalDate toDate
                                                        );

    /**
     * Retrieves a stream of repositories that the user has committed to within the specified
     * date range.
     *
     * @param login    the GitHub username
     * @param fromDate start date for the commits range
     * @param toDate   end date for the commits range
     * @return stream of repository information
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}/repositories/committed-to/from/{fromDate}/to/{toDate}")
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<SseEvent<String>> getRepositoriesCommittedTo(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login,
            @PathParam("fromDate") LocalDate fromDate,
            @PathParam("toDate") LocalDate toDate
                                                      );

}
