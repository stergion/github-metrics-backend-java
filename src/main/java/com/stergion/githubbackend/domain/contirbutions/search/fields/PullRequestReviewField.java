package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum PullRequestReviewField implements SearchField {
    // COMMON FIELDS
    USER_LOGIN(CommonField.USER_LOGIN.fieldName()),
    OWNER(CommonField.OWNER.fieldName()),
    NAME(CommonField.NAME.fieldName()),

    // REVIEW SPECIFIC FIELDS

    // categorical
    STATE("STATE"),

    // time
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    PUBLISHED_AT("publishedAt"),
    LAST_EDITED_AT("lastEditedAt"),

    // range
    ;

    private final String fieldName;

    PullRequestReviewField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
