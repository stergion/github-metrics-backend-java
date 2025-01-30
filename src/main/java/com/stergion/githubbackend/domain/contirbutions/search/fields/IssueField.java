package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueField implements SearchField {
    // categorical
    STATE("STATE"),
    CLOSER("closer"),

    // time
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    CLOSED_AT("closedAt"),

    // range
    REACTIONS("reactionsCount");

    private final String fieldName;

    IssueField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
