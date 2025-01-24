package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum PullRequestField implements SearchField {
    STATE("STATE"),
    MERGED_AT("mergedAt"),
    UPDATED_AT("updatedAt"),
    CLOSED_AT("closedAt");

    private final String fieldName;

    PullRequestField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
