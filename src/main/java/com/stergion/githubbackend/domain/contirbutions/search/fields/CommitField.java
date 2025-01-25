package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum CommitField implements SearchField {
    // categorical

    // time
    COMMITED_DATE("committedDate"),
    PUSHED_DATE("pushedDate"),

    //range
    FILES_COUNT("filesCount"),
    ADDITIONS("additions"),
    DELETIONS("deletions");

    private final String fieldName;

    CommitField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
