package com.stergion.githubbackend.domain.utils.types;

public record Language(String name, int size, float percentage) {

    @Override
    public String toString() {
        return "{\"name\": %s, \"size\": %s, \"percentage\": %s}".formatted(name, size, percentage);

    }
}
