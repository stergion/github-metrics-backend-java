package com.stergion.githubbackend.domain.contirbutions.search.fields;

public interface RangeableField<T> {
    SearchField getField();

    String getFieldName();

    Class<T> getValueType();
}