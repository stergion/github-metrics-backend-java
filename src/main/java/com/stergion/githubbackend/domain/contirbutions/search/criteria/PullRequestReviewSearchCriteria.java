package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestReviewRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestReviewTimeField;
import com.stergion.githubbackend.domain.utils.types.PullRequestReviewState;

import java.util.Optional;

public class PullRequestReviewSearchCriteria
        extends BaseSearchCriteria<PullRequestReviewRangeField, PullRequestReviewTimeField> {
    private final PullRequestReviewState state;

    private PullRequestReviewSearchCriteria(Builder builder) {
        super(builder);
        this.state = builder.state;
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate() {
    }

    public Optional<PullRequestReviewState> getState() {
        return Optional.ofNullable(state);
    }


    public static class Builder
            extends BaseBuilder<Builder, PullRequestReviewRangeField, PullRequestReviewTimeField> {
        private PullRequestReviewState state;

        public Builder state(PullRequestReviewState state) {
            this.state = state;
            return this;
        }

        @Override
        public PullRequestReviewSearchCriteria build() {
            return new PullRequestReviewSearchCriteria(this);
        }
    }
}
