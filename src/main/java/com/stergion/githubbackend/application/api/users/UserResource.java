package com.stergion.githubbackend.application.api.users;

import com.stergion.githubbackend.application.mapper.RepositoryMapper;
import com.stergion.githubbackend.application.mapper.UserMapper;
import com.stergion.githubbackend.application.response.NameWithOwnerResponse;
import com.stergion.githubbackend.application.response.UserResponse;
import com.stergion.githubbackend.application.service.UserDataManagementService;
import com.stergion.githubbackend.domain.repositories.RepositoryDTO;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.domain.users.UserService;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import java.util.List;

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
    public Object getUser(String login) {
        Log.info("Fetching user: " + login);
        try {
            var usr = userService.getUser(login);
            List<RepositoryDTO> repos = repositoryService.getRepositories(usr.repositories());
            UserResponse userResponse = userMapper.toResponse(usr, repos);
            return ResponseBuilder.ok(userResponse).build();
        } catch (Exception e) {
            Log.error(e);
            return ResponseBuilder.serverError().build();
        }

    }

    @POST
    public RestResponse<Object> createUser(String login) {
        Log.info("Creating user: " + login);
        try {
            userDataManagementService.setupUser(login);
        } catch (Exception e) {
            Log.error(e);
            return ResponseBuilder.serverError().build();
        }

        Log.info("User '" + login + "' was created successfully!!!");
        return ResponseBuilder.create(201).build();
    }

    @PUT
    public RestResponse<Object> updateUser(String login) {
        Log.info("Updating user: " + login);
        try {
            userDataManagementService.updateUser(login);
        } catch (Exception e) {
            Log.error(e);
            return ResponseBuilder.serverError().build();
        }

        Log.info("User '" + login + "' was updated successfully!!!");
        return ResponseBuilder.ok().build();
    }

    @DELETE
    public RestResponse<Object> deleteUser(String login) {
        Log.info("Deleting user: " + login);
        try {
            userDataManagementService.deleteUser(login);
        } catch (Exception e) {
            Log.error(e);
            return ResponseBuilder.serverError().build();
        }

        Log.info("User '" + login + "' was deleted successfully!!!");
        return ResponseBuilder.create(200).build();
    }

    @GET
    @Path("/repositories")
    public Object getUserRepositories(String login) {
        Log.info("Getting user: " + login + " repositories");
        try {
            List<RepositoryDTO> repos = userService.getUserRepositories(login);
            List<NameWithOwnerResponse> nameWithOwners = repos.stream()
                                                              .map(repositoryMapper::toNameWithOwnerResponse)
                                                              .toList();
            return ResponseBuilder.ok(nameWithOwners).build();
        } catch (Exception e) {
            Log.error(e);
            return ResponseBuilder.serverError().build();
        }

    }
}