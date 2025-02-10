package com.stergion.githubbackend.application.api.contributions;

import com.stergion.githubbackend.application.request.search.CommitSearchRequest;
import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.services.CommitService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;


@Path("/api/commits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public final class CommitResource
        extends ContributionResource<CommitSearchCriteria, CommitSearchCriteria.Builder,
        CommitSearchRequest> {
    @Inject
    CommitService commitService;

    @POST
    @Path("/search")
    public Uni<PagedResponse<Commit>> findContributions(CommitSearchRequest request) {
        var builder = CommitSearchCriteria.builder();

        var criteria = buildSearchCriteria(builder, request);
        Log.debug("Commit search criteria: \n" + criteria);
        return commitService.search(criteria);
    }

    @Override
    void setRangeFields(CommitSearchCriteria.Builder builder,
                        CommitSearchRequest request) {
        request.rangeFilters().forEach(builder::addRangeFilter);
    }

    @Override
    void setTimeFields(CommitSearchCriteria.Builder builder,
                       CommitSearchRequest request) {
        request.timeFilters().forEach(builder::addTimeFilter);
    }

    @Override
    void setSpecificSearchFields(CommitSearchCriteria.Builder builder,
                                 CommitSearchRequest request) {
    }
}