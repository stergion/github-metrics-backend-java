package com.stergion.githubbackend.core.metrics;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

@Path("/api/metrics/{login}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MetricsResource {
    @GET
    public RestResponse<JsonObject> getMetrics(String login) {
        // TODO: Implement route

        var obj = Json.createObjectBuilder()
                      .add("login", login)
                      .add("message", "GET /metrics/" + login)
                      .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }

    @POST
    public RestResponse<JsonObject> generateMetrics(String login) {
        // TODO: Implement route

        var obj = Json.createObjectBuilder()
                      .add("login", login)
                      .add("message", "Metrics generated")
                      .build();
        System.out.println(obj);
        return ResponseBuilder.ok(obj).build();
    }
}