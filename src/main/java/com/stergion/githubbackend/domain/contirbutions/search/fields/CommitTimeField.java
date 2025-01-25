package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum CommitTimeField implements TimeField {
    // Common Time Fields
    CREATED_AT(CommonField.CREATED_AT),

    // Commit Specific Time Fields
    COMMITTED_AT(CommitField.COMMITED_DATE),
    PUSHED_AT(CommitField.PUSHED_DATE);

    private final SearchField field;

    CommitTimeField(SearchField field) {
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
