package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueCommentField implements SearchField {
    PUBLISHED_AT("publishedAt"),
    UPDATED_AT("updatedAt");

    private final String fieldName;

    IssueCommentField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
