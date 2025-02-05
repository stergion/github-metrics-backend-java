package com.stergion.githubbackend.infrastructure.persistence.utils.types;

public record NameWithOwner(String owner, String name) {

    @Override
    public String toString() {
        return "{ owner: %s, name: %s }".formatted(owner, name);
    }
}
