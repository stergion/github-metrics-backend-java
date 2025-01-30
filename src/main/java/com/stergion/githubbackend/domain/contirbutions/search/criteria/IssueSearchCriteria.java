package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueTimeField;
import com.stergion.githubbackend.domain.utils.types.IssueState;

import java.util.Optional;

public class IssueSearchCriteria extends BaseSearchCriteria<IssueRangeField, IssueTimeField> {
    private final IssueState state;
    private final String closer;

    private IssueSearchCriteria(Builder builder) {
        super(builder);
        this.state = builder.state;
        this.closer = builder.closer;
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate() {
    }

    public Optional<IssueState> getState() {
        return Optional.ofNullable(state);
    }

    public Optional<String> getCloser() {
        return Optional.ofNullable(closer);
    }


    public static class Builder extends BaseBuilder<Builder, IssueRangeField, IssueTimeField> {
        private IssueState state;
        private String closer;


        public Builder state(IssueState state) {
            this.state = state;
            return this;
        }

        public Builder closer(String closer) {
            this.closer = closer;
            return this;
        }

        @Override
        public IssueSearchCriteria build() {
            return new IssueSearchCriteria(this);
        }
    }
}
