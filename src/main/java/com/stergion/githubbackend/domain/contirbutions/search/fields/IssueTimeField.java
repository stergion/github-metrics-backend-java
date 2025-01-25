package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum IssueTimeField implements TimeField {
    // Common Time Fields
    CREATED_AT(CommonField.CREATED_AT),

    // Issue Specific Time Fields
    UPDATED_AT(IssueField.UPDATED_AT),
    CLOSED_AT(IssueField.CLOSED_AT);

    private final SearchField field;

    IssueTimeField(SearchField field) {
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
