package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum PullRequestReviewRangeField implements RangeField {
    ;

    private final SearchField field;

    PullRequestReviewRangeField(SearchField field) {
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