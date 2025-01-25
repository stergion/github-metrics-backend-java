package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitTimeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueTimeField;
import com.stergion.githubbackend.domain.utils.types.IssueState;

import java.time.LocalDateTime;
import java.util.Optional;

public class IssueSearchCriteria extends BaseSearchCriteria<IssueRangeField, IssueTimeField> {
    private final String owner;
    private final String name;
    private final IssueState state;
    private final String closer;

    private IssueSearchCriteria(Builder builder) {
        super(builder);
        this.state = builder.state;
        this.owner = builder.owner;
        this.name = builder.name;
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

    public Optional<String> getOwner() {
        return Optional.ofNullable(owner);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getCloser() {
        return Optional.ofNullable(closer);
    }


    public static class Builder extends BaseBuilder<Builder, IssueRangeField, IssueTimeField> {
        private String owner;
        private String name;
        private IssueState state;
        private String closer;


        public Builder state(IssueState state) {
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

        public Builder closer(String closer) {
            this.closer = closer;
            return this;
        }

        public IssueSearchCriteria build() {
            return new IssueSearchCriteria(this);
        }
    }
}
