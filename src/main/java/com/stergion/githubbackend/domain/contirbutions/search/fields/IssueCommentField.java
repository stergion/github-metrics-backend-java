package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueCommentField implements SearchField {
    // COMMON FIELDS
    USER_LOGIN(CommonField.USER_LOGIN.fieldName()),
    OWNER(CommonField.OWNER.fieldName()),
    NAME(CommonField.NAME.fieldName()),

    // ISSUE COMMENT SPECIFIC FIELDS

    // categorical

    // time
    CREATED_AT("createdAt"),
    PUBLISHED_AT("publishedAt"),
    UPDATED_AT("updatedAt"),
    LAST_EDITED_AT("lastEditedAt"),

    // range
    ;

    private final String fieldName;

    IssueCommentField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
