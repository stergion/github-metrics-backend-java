package com.stergion.githubbackend.infrastructure.persistence.utils;

public enum DatabaseType {
    MONGO("mongo"),
    POSTGRES("postgres");

    private final String value;

    DatabaseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DatabaseType fromString(String value) {
        for (DatabaseType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown database type: " + value);
    }
}