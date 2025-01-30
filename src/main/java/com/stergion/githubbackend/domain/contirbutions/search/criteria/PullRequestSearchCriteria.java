package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitTimeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestTimeField;
import com.stergion.githubbackend.domain.utils.types.PullRequestState;

import java.time.LocalDateTime;
import java.util.Optional;

public class PullRequestSearchCriteria extends BaseSearchCriteria<PullRequestRangeField, PullRequestTimeField> {
    private final String owner;
    private final String name;
    private final PullRequestState state;

    private PullRequestSearchCriteria(Builder builder) {
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

    public Optional<PullRequestState> getState() {
        return Optional.ofNullable(state);
    }

    public Optional<String> getOwner() {
        return Optional.ofNullable(owner);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }


    public static class Builder extends BaseBuilder<Builder, PullRequestRangeField, PullRequestTimeField> {
        private PullRequestState state;
        private String owner;
        private String name;

        public Builder state(PullRequestState state) {
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

        @Override
        public PullRequestSearchCriteria build() {
            return new PullRequestSearchCriteria(this);
        }
    }
}
