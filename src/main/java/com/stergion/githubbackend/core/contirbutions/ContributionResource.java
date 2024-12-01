package com.stergion.githubbackend.core.contirbutions;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

/**
    @apiNote Fetches a user's contributions from GitHub and saves them in MongoDB
 */

@Path("/api/contributions/{login}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContributionResource {
    @Context
    UriInfo uriInfo;

    @Context
    Request request;

    @GET
    public RestResponse<JsonObject> getContributions(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "GET /contributions/" + login)
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }

    @POST
    public RestResponse<JsonObject> createContributions(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "Contributions registered")
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }

    @PUT
    public RestResponse<JsonObject> updateContributions(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "Contribution updated!")
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }

    @DELETE
    public RestResponse<JsonObject> deleteContributions(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "Contribution deleted!")
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }
}