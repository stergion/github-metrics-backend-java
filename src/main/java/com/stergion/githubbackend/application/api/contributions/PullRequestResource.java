package com.stergion.githubbackend.application.api.contributions;

import com.stergion.githubbackend.application.request.search.PullRequestSearchRequest;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestDTO;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.services.PullRequestService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;


@Path("/api/pulls")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class PullRequestResource
        extends ContributionResource<PullRequestSearchCriteria,
        PullRequestSearchCriteria.Builder, PullRequestSearchRequest> {
    @Inject
    PullRequestService pullRequestService;


    @POST
    @Path("/search")
    public Uni<PagedResponse<PullRequestDTO>> findContributions(PullRequestSearchRequest request) {
        var builder = PullRequestSearchCriteria.builder();

        var criteria = buildSearchCriteria(builder, request);
        Log.debug("Pull Request search criteria: \n" + criteria);
        return pullRequestService.search(criteria);
    }

    @Override
    void setRangeFields(PullRequestSearchCriteria.Builder builder,
                        PullRequestSearchRequest request) {
        request.rangeFilters().forEach(builder::addRangeFilter);
    }

    @Override
    void setTimeFields(PullRequestSearchCriteria.Builder builder,
                       PullRequestSearchRequest request) {
        request.timeFilters().forEach(builder::addTimeFilter);
    }

    @Override
    void setSpecificSearchFields(PullRequestSearchCriteria.Builder builder,
                                 PullRequestSearchRequest request) {
    }
}