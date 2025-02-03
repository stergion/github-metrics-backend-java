package com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes;

public class Language {
    public String name;
    public int size;
    public float percentage;

    @Override
    public String toString() {
        return "{name: %s, size: %s, percentage: %s}".formatted(name, size, percentage);
    }
}
