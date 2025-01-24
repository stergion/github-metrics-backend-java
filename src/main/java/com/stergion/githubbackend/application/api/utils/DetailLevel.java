package com.stergion.githubbackend.application.api.utils;

public enum DetailLevel {
    BASIC, FULL;

    public static DetailLevel fromString(String value) {
        return value == null ? BASIC : valueOf(value.toUpperCase());
    }
}