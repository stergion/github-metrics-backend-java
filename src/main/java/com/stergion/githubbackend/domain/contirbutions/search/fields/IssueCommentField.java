package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum IssueCommentField implements SearchField {
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
