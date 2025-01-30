package com.stergion.githubbackend.domain.contirbutions.search.fields;

public interface RangeableField<T> {
    String getField();

    Class<T> getValueType();
}