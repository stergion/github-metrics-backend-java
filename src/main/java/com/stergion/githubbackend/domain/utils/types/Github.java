package com.stergion.githubbackend.domain.utils.types;

public record Github(String id, String url) {
    @Override
    public String toString() {
        return "{\"id\": %s, \"url\": %s}".formatted(id, url);
    }
}
