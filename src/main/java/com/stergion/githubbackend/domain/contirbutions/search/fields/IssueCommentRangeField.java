package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueCommentRangeField implements RangeField {
    ;
    private final String field;

    IssueCommentRangeField(String field) {
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