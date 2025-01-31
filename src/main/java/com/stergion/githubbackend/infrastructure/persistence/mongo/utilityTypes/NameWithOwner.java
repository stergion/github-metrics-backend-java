package com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes;

public record NameWithOwner(String owner, String name) {

    @Override
    public String toString() {
        return "{\"owner\": %s, \"name\": %s}".formatted(owner, name);
    }
}
