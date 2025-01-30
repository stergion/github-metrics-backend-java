package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitTimeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestTimeField;
import com.stergion.githubbackend.domain.utils.types.PullRequestState;

import java.time.LocalDateTime;
import java.util.Optional;

public class PullRequestSearchCriteria extends BaseSearchCriteria<PullRequestRangeField, PullRequestTimeField> {
    private final PullRequestState state;

    private PullRequestSearchCriteria(Builder builder) {
        super(builder);
        this.state = builder.state;
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate() {
    }

    public Optional<PullRequestState> getState() {
        return Optional.ofNullable(state);
    }


    public static class Builder extends BaseBuilder<Builder, PullRequestRangeField, PullRequestTimeField> {
        private PullRequestState state;

        public Builder state(PullRequestState state) {
            this.state = state;
            return this;
        }

        @Override
        public PullRequestSearchCriteria build() {
            return new PullRequestSearchCriteria(this);
        }
    }
}
