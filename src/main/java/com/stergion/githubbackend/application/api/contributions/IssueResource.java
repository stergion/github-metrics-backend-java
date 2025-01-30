package com.stergion.githubbackend.application.api.contributions;

import com.stergion.githubbackend.application.request.search.IssueSearchRequest;
import com.stergion.githubbackend.domain.contirbutions.dto.IssueDTO;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.services.IssueService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;


@Path("/api/issues")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class IssueResource
        extends ContributionResource<IssueSearchCriteria, IssueSearchCriteria.Builder,
        IssueSearchRequest> {
    @Inject
    IssueService issueService;


    @POST
    @Path("/search")
    public Uni<PagedResponse<IssueDTO>> findContributions(IssueSearchRequest request) {
        var builder = IssueSearchCriteria.builder();

        var criteria = buildSearchCriteria(builder, request);
        Log.debug("Issue search criteria: \n" + criteria);
        return issueService.search(criteria);
    }

    @Override
    void setRangeFields(IssueSearchCriteria.Builder builder,
                        IssueSearchRequest request) {
        request.rangeFilters().forEach(builder::addRangeFilter);
    }

    @Override
    void setTimeFields(IssueSearchCriteria.Builder builder,
                       IssueSearchRequest request) {
        request.timeFilters().forEach(builder::addTimeFilter);
    }

    @Override
    void setSpecificSearchFields(IssueSearchCriteria.Builder builder,
                                 IssueSearchRequest request) {
    }
}