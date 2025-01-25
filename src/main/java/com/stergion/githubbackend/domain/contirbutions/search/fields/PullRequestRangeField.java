package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum PullRequestRangeField implements RangeField {
    COMMITS(PullRequestField.COMMITS),
    COMMENTS(PullRequestField.COMMENTS),
    REACTIONS(PullRequestField.REACTIONS),
    CLOSING_ISSUES(PullRequestField.CLOSING_ISSUES);

    private final SearchField field;

    PullRequestRangeField(SearchField field) {
        this.field = field;
    }

    @Override
    public SearchField getField() {
        return field;
    }

    @Override
    public String getFieldName() {
        return field.fieldName();
    }

    @Override
    public Class<Integer> getValueType() {
        return Integer.class;
    }
}