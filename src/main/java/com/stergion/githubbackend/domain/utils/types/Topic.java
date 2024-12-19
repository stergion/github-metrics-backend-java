package com.stergion.githubbackend.domain.utils.types;

public record Topic(String name) {

    @Override
    public String toString() {
        return "{\"name\": %s}".formatted(name);
    }
}
