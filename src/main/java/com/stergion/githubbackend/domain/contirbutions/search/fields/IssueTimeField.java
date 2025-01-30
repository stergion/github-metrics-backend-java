package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum IssueTimeField implements TimeField {
    CREATED_AT(IssueField.CREATED_AT.fieldName()),
    UPDATED_AT(IssueField.UPDATED_AT.fieldName()),
    CLOSED_AT(IssueField.CLOSED_AT.fieldName());

    private final String field;

    IssueTimeField(String field) {
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
