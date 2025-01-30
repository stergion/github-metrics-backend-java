package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum PullRequestTimeField implements TimeField {
    CREATED_AT(PullRequestField.CREATED_AT.fieldName()),
    MERGED_AT(PullRequestField.MERGED_AT.fieldName()),
    UPDATED_AT(PullRequestField.UPDATED_AT.fieldName()),
    CLOSED_AT(PullRequestField.CLOSED_AT.fieldName());

    private final String field;

    PullRequestTimeField(String field) {
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
