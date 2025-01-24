package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum PullRequestReviewField implements SearchField {
    STATE("STATE"),
    UPDATED_AT("updatedAt"),
    PUBLISHED_AT("publishedAt");

    private final String fieldName;

    PullRequestReviewField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
