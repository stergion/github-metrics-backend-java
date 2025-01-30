package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitTimeField;

import java.util.Optional;

public class CommitSearchCriteria extends BaseSearchCriteria<CommitRangeField, CommitTimeField> {
    private final String owner;
    private final String name;

    private CommitSearchCriteria(Builder builder) {
        super(builder);
        this.owner = builder.owner;
        this.name = builder.name;
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate() {
    }

    public Optional<String> getOwner() {
        return Optional.ofNullable(owner);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }


    public static class Builder extends BaseBuilder<Builder, CommitRangeField, CommitTimeField> {
        private String owner;
        private String name;

        public Builder owner(String name) {
            this.owner = name;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public CommitSearchCriteria build() {
            return new CommitSearchCriteria(this);
        }
    }
}
