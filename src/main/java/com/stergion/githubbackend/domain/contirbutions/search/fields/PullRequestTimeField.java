package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum PullRequestTimeField implements TimeField {
    // Common Time Fields
    CREATED_AT(CommonField.CREATED_AT),

    // Pull Request Specific Time Fields
    MERGED_AT(PullRequestField.MERGED_AT),
    UPDATED_AT(PullRequestField.UPDATED_AT),
    CLOSED_AT(PullRequestField.CLOSED_AT);

    private final SearchField field;

    PullRequestTimeField(SearchField field) {
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
