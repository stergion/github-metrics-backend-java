package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueRangeField implements RangeField {
    REACTIONS(IssueField.REACTIONS.fieldName());

    private final String field;

    IssueRangeField(String field) {
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