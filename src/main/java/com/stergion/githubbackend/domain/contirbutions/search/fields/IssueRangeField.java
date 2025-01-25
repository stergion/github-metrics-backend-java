package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueRangeField implements RangeField {
    REACTIONS(IssueField.REACTIONS);

    private final SearchField field;

    IssueRangeField(SearchField field) {
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