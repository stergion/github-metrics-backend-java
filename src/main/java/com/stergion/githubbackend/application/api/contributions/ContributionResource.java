package com.stergion.githubbackend.application.api.contributions;

import com.stergion.githubbackend.application.request.search.ContributionSearchRequest;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.BaseSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.RangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.TimeField;

public abstract sealed class ContributionResource<
        C extends BaseSearchCriteria<? extends RangeField, ? extends TimeField>,
        B extends BaseSearchCriteria.BaseBuilder<B, ? extends RangeField, ? extends TimeField>,
        R extends ContributionSearchRequest>
        permits CommitResource, IssueResource, IssueCommentResource, PullRequestResource,
        PullRequestReviewResource {

    protected C buildSearchCriteria(B builder, R request) {
        setBaseSearchFields(builder, request);
        setSpecificSearchFields(builder, request);
        setRangeFields(builder, request);
        setTimeFields(builder, request);

        return (C) builder.build();
    }

    protected void setBaseSearchFields(B builder, R request) {
        builder.page(request.page())
               .size(request.size())
               .sortBy(request.sortBy())
               .ascending(request.ascending())
               .userLogin(request.userLogin())
               .owner(request.owner())
               .name(request.name());
    }

    abstract void setSpecificSearchFields(B builder, R request);

    abstract void setRangeFields(B builder, R request);

    abstract void setTimeFields(B builder, R request);
}
