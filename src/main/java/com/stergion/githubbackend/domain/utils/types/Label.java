package com.stergion.githubbackend.domain.utils.types;

public record Label(String name, String description) {
    @Override
    public String toString() {
        return "{\"user\": %s, \"repository\": %s}".formatted(name, description);

    }
}
