package com.stergion.githubbackend.application.api.users;

import com.stergion.githubbackend.application.api.utils.DetailLevel;
import com.stergion.githubbackend.application.mapper.RepositoryMapper;
import com.stergion.githubbackend.application.mapper.UserMapper;
import com.stergion.githubbackend.application.response.UserResponse;
import com.stergion.githubbackend.application.service.UserDataManagementService;
import com.stergion.githubbackend.domain.repositories.RepositoryDTO;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.domain.users.UserService;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@Path("/api/users/{login}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;
    @Inject
    RepositoryService repositoryService;
    @Inject
    UserMapper userMapper;
    @Inject
    RepositoryMapper repositoryMapper;
    @Inject
    UserDataManagementService userDataManagementService;

    @GET
    public RestResponse<UserResponse> getUser(String login) {
        Log.info("Fetching user: " + login);

        var usr = userService.getUser(login);

        List<RepositoryDTO> repos = repositoryService.getRepositories(usr.repositories());
        UserResponse userResponse = userMapper.toResponse(usr, repos);

        return RestResponse.ok(userResponse);
    }

    @POST
    public RestResponse<String> createUser(String login) {
        Log.info("Creating user: " + login);
        userDataManagementService.setupUser(login);

        Log.info("User '" + login + "' was created successfully!!!");
        return RestResponse.created(URI.create("/api/users/" + login));
    }

    @PUT
    public RestResponse<String> updateUser(String login) {
        Log.info("Updating user: " + login);

        userDataManagementService.updateUser(login);

        var msg = "User '" + login + "' updated successfully";
        Log.info(msg);
        return RestResponse.ok(msg);
    }

    @DELETE
    public RestResponse<String> deleteUser(String login) {
        Log.info("Deleting user: " + login);

        userDataManagementService.deleteUser(login);


        String msg = "User '" + login + "' was deleted successfully";
        Log.info(msg);
        return RestResponse.ok(msg);
    }

    @GET
    @Path("/repositories")
    public RestResponse<List<?>> getUserRepositories(String login,
                                                     @RestQuery @DefaultValue("basic") String detail) {
        Log.info("Getting user: " + login + " repositories");

        var detailLevel = DetailLevel.fromString(detail);
        List<RepositoryDTO> repos = userService.getUserRepositories(login);

        Function<RepositoryDTO, ?> mapper = switch(detailLevel) {
            case FULL -> repositoryMapper::toRepositoryResponse;
            case BASIC -> repositoryMapper::toNameWithOwnerResponse;
        };

        return RestResponse.ok(repos.stream()
                                    .map(mapper)
                                    .toList());
    }
}