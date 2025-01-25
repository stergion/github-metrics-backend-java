package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueCommentRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueCommentTimeField;

import java.util.Optional;

public class IssueCommentSearchCriteria
        extends BaseSearchCriteria<IssueCommentRangeField, IssueCommentTimeField> {
    private final String owner;
    private final String name;

    private IssueCommentSearchCriteria(Builder builder) {
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


    public static class Builder
            extends BaseBuilder<Builder, IssueCommentRangeField, IssueCommentTimeField> {
        private String owner;
        private String name;

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public IssueCommentSearchCriteria build() {
            return new IssueCommentSearchCriteria(this);
        }
    }
}
