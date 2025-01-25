package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum PullRequestReviewTimeField implements TimeField {
    // Common Time Fields
    CREATED_AT(CommonField.CREATED_AT),

    // Review Specific Time Fields
    UPDATED_AT(PullRequestReviewField.UPDATED_AT),
    PUBLISHED_AT(PullRequestReviewField.PUBLISHED_AT),
    LAST_EDITED_AT(PullRequestReviewField.LAST_EDITED_AT);

    private final SearchField field;

    PullRequestReviewTimeField(SearchField field) {
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
