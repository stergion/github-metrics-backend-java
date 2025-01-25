package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestReviewRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestReviewTimeField;
import com.stergion.githubbackend.domain.utils.types.PullRequestReviewState;

import java.util.Optional;

public class PullRequestReviewSearchCriteria
        extends BaseSearchCriteria<PullRequestReviewRangeField, PullRequestReviewTimeField> {
    private final String owner;
    private final String name;
    private final PullRequestReviewState state;

    private PullRequestReviewSearchCriteria(Builder builder) {
        super(builder);
        this.state = builder.state;
        this.owner = builder.owner;
        this.name = builder.name;
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

    public Optional<String> getOwner() {
        return Optional.ofNullable(owner);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }


    public static class Builder
            extends BaseBuilder<Builder, PullRequestReviewRangeField, PullRequestReviewTimeField> {
        private PullRequestReviewState state;
        private String owner;
        private String name;

        public Builder state(PullRequestReviewState state) {
            this.state = state;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public PullRequestReviewSearchCriteria build() {
            return new PullRequestReviewSearchCriteria(this);
        }
    }
}
