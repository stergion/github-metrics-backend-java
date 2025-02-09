package com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes;

public record Label(String name, String description) {
    @Override
    public String toString() {
        return "{ name: '" + name + '\'' +
                ", description: '" + description + '\'' +
                '}';
    }
}
