package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum CommitTimeField implements TimeField {
    COMMITED_DATE(CommitField.COMMITED_DATE.fieldName()),
    PUSHED_DATE(CommitField.PUSHED_DATE.fieldName());

    private final String field;

    CommitTimeField(String field) {
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
