package com.stergion.githubbackend.client;

import com.stergion.githubbackend.client.models.UserInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

public interface UserRoutes {

    /**
     * Retrieves detailed information about a GitHub user.
     *
     * @param login the GitHub username
     * @return user information
     * @throws WebApplicationException if the user doesn't exist or other API errors occur
     */
    @GET
    @Path("/user/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    UserInfo getUserInfo(
            @NotBlank(message = "Login cannot be blank")
            @PathParam("login") String login
    );
}
