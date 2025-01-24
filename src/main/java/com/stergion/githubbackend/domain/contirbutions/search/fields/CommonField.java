package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum CommonField implements SearchField {
    USER_LOGIN("user.login"),
    OWNER("repository.owner"),
    NAME("repository.name"),
    CREATED_AT("createdAt");

    private final String fieldName;

    CommonField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
