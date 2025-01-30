package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitTimeField;

import java.util.Optional;

public class CommitSearchCriteria extends BaseSearchCriteria<CommitRangeField, CommitTimeField> {

    private CommitSearchCriteria(Builder builder) {
        super(builder);
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate() {
    }

    public static class Builder extends BaseBuilder<Builder, CommitRangeField, CommitTimeField> {
        @Override
        public CommitSearchCriteria build() {
            return new CommitSearchCriteria(this);
        }
    }
}
