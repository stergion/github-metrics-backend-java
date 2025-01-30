package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum PullRequestField implements SearchField {
    // categorical
    STATE("STATE"),

    // time
    CREATED_AT("createdAt"),
    MERGED_AT("mergedAt"),
    UPDATED_AT("updatedAt"),
    CLOSED_AT("closedAt"),

    // range
    COMMITS("commitsCount"),
    COMMENTS("comments"),
    REACTIONS("reactionsCount"),
    CLOSING_ISSUES("closingIssuesReferencesCount");


    private final String fieldName;

    PullRequestField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
