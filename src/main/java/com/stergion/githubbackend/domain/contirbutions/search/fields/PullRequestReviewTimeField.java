package com.stergion.githubbackend.domain.contirbutions.search.fields;

import java.time.LocalDateTime;

public enum PullRequestReviewTimeField implements TimeField {
    CREATED_AT(PullRequestReviewField.CREATED_AT.fieldName()),
    UPDATED_AT(PullRequestReviewField.UPDATED_AT.fieldName()),
    PUBLISHED_AT(PullRequestReviewField.PUBLISHED_AT.fieldName()),
    LAST_EDITED_AT(PullRequestReviewField.LAST_EDITED_AT.fieldName());

    private final String field;

    PullRequestReviewTimeField(String field) {
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
