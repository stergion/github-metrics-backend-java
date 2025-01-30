package com.stergion.githubbackend.domain.contirbutions.search.fields;

public enum CommonField implements SearchField {
    // When adding new fields here also add them in all the contribution field enums
    USER_LOGIN("user.login"),
    OWNER("repository.owner"),
    NAME("repository.name");

    private final String fieldName;

    CommonField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String fieldName() {
        return fieldName;
    }
}
