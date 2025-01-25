package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum CommitRangeField implements RangeField {
    ADDITIONS(CommitField.ADDITIONS),
    DELETIONS(CommitField.DELETIONS),
    FILES_COUNT(CommitField.FILES_COUNT);

    private final SearchField field;

    CommitRangeField(SearchField field) {
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
    public Class<Integer> getValueType() {
        return Integer.class;
    }
}