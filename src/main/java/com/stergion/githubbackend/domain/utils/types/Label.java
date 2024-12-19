package com.stergion.githubbackend.domain.utils.types;

public record Label(String name, String description) {
    @Override
    public String toString() {
        return "{\"name\": %s, \"description\": %s}".formatted(name, description);

    }
}
