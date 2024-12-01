package com.stergion.githubbackend.core.users;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

@Path("/api/users/{login}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    public RestResponse<JsonObject> getUser(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "GET  /users/" + login)
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }

    @POST
    public RestResponse<JsonObject> createUser(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "User created!")
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }

    @PUT
    public RestResponse<JsonObject> updateUser(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "user updated!")
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }

    @DELETE
    public RestResponse<JsonObject> deleteUser(String login) {
        // TODO: Implement route

        var obj  = Json.createObjectBuilder()
                .add("login", login)
                .add("message", "user deleted!")
                .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }
}