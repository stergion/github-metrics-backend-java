package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum PullRequestRangeField implements RangeField {
    COMMITS(PullRequestField.COMMITS.fieldName()),
    COMMENTS(PullRequestField.COMMENTS.fieldName()),
    REACTIONS(PullRequestField.REACTIONS.fieldName()),
    CLOSING_ISSUES(PullRequestField.CLOSING_ISSUES.fieldName());

    private final String field;

    PullRequestRangeField(String field) {
        this.field = field;
    }


    @Override
    public String getField() {
        return field;
    }

    @Override
    public Class<Integer> getValueType() {
        return Integer.class;
    }
}