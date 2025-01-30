package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum CommitRangeField implements RangeField {
    ADDITIONS(CommitField.ADDITIONS.fieldName()),
    DELETIONS(CommitField.DELETIONS.fieldName()),
    FILES_COUNT(CommitField.FILES_COUNT.fieldName());

    private final String field;

    CommitRangeField(String field) {
        this.field = field;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public Class<Integer> getValueType() {
        return Integer.class;
    }
}