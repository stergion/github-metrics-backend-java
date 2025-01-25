package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum IssueCommentTimeField implements TimeField {
    // Common Time Fields
    CREATED_AT(CommonField.CREATED_AT),

    // Issue Comment Specific Time Fields
    UPDATED_AT(IssueCommentField.UPDATED_AT),
    PUBLISHED_AT(IssueCommentField.PUBLISHED_AT),
    LAST_EDITED_AT(IssueCommentField.LAST_EDITED_AT);

    private final SearchField field;

    IssueCommentTimeField(SearchField field) {
        this.field = field;
    }

    @Override
    public SearchField getField() {
        return field;
    }

    @Override
    public String getFieldName() {
        return field.fieldName();
    }

    @Override
    public Class<LocalDateTime> getValueType() {
        return LocalDateTime.class;
    }
}
