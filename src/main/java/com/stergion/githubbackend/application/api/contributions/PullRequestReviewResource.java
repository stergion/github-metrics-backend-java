package com.stergion.githubbackend.application.api.contributions;

import com.stergion.githubbackend.application.request.search.PullRequestReviewSearchRequest;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestReviewDTO;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.services.PullRequestReviewService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;


@Path("/api/reviews")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class PullRequestReviewResource
        extends ContributionResource<PullRequestReviewSearchCriteria,
        PullRequestReviewSearchCriteria.Builder, PullRequestReviewSearchRequest> {
    @Inject
    PullRequestReviewService pullRequestReviewService;


    @POST
    @Path("/search")
    public Uni<PagedResponse<PullRequestReviewDTO>> findContributions(
            PullRequestReviewSearchRequest request) {
        var builder = PullRequestReviewSearchCriteria.builder();

        var criteria = buildSearchCriteria(builder, request);
        Log.debug("Review search criteria: \n" + criteria);
        return pullRequestReviewService.search(criteria);
    }

    @Override
    void setRangeFields(PullRequestReviewSearchCriteria.Builder builder,
                        PullRequestReviewSearchRequest request) {
        request.rangeFilters().forEach(builder::addRangeFilter);
    }

    @Override
    void setTimeFields(PullRequestReviewSearchCriteria.Builder builder,
                       PullRequestReviewSearchRequest request) {
        request.timeFilters().forEach(builder::addTimeFilter);
    }

    @Override
    void setSpecificSearchFields(PullRequestReviewSearchCriteria.Builder builder,
                                 PullRequestReviewSearchRequest request) {
    }
}