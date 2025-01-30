package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum IssueCommentTimeField implements TimeField {
    CREATED_AT(IssueCommentField.CREATED_AT.fieldName()),
    UPDATED_AT(IssueCommentField.UPDATED_AT.fieldName()),
    PUBLISHED_AT(IssueCommentField.PUBLISHED_AT.fieldName()),
    LAST_EDITED_AT(IssueCommentField.LAST_EDITED_AT.fieldName());

    private final String field;

    IssueCommentTimeField(String field) {
        this.field = field;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public Class<LocalDateTime> getValueType() {
        return LocalDateTime.class;
    }
}
