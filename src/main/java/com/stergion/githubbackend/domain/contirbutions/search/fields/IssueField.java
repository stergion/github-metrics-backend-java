package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueField implements SearchField {
    STATE("STATE"),
    UPDATED_AT("updatedAt"),
    CLOSED_AT("closedAt"),
    CLOSER("closer");

    private final String fieldName;

    IssueField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
