package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueCommentRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueCommentTimeField;

import java.util.Optional;

public class IssueCommentSearchCriteria
        extends BaseSearchCriteria<IssueCommentRangeField, IssueCommentTimeField> {

    private IssueCommentSearchCriteria(Builder builder) {
        super(builder);
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate() {
    }

    public static class Builder
            extends BaseBuilder<Builder, IssueCommentRangeField, IssueCommentTimeField> {
        @Override
        public IssueCommentSearchCriteria build() {
            return new IssueCommentSearchCriteria(this);
        }
    }
}
