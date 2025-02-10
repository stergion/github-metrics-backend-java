package com.stergion.githubbackend.application.api.contributions;

import com.stergion.githubbackend.application.request.search.IssueCommentSearchRequest;
import com.stergion.githubbackend.domain.contirbutions.models.IssueCommentDTO;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.services.IssueCommentService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;


@Path("/api/issue-comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class IssueCommentResource
        extends ContributionResource<IssueCommentSearchCriteria,
        IssueCommentSearchCriteria.Builder, IssueCommentSearchRequest> {
    @Inject
    IssueCommentService issueCommentService;


    @POST
    @Path("/search")
    public Uni<PagedResponse<IssueCommentDTO>> findContributions(
            IssueCommentSearchRequest request) {
        var builder = IssueCommentSearchCriteria.builder();

        var criteria = buildSearchCriteria(builder, request);
        Log.debug("Issue Comment search criteria: \n" + criteria);
        return issueCommentService.search(criteria);
    }

    @Override
    void setRangeFields(IssueCommentSearchCriteria.Builder builder,
                        IssueCommentSearchRequest request) {
        request.rangeFilters().forEach(builder::addRangeFilter);
    }

    @Override
    void setTimeFields(IssueCommentSearchCriteria.Builder builder,
                       IssueCommentSearchRequest request) {
        request.timeFilters().forEach(builder::addTimeFilter);
    }

    @Override
    void setSpecificSearchFields(IssueCommentSearchCriteria.Builder builder,
                                 IssueCommentSearchRequest request) {
    }
}