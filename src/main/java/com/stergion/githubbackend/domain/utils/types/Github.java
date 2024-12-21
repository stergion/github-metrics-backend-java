package com.stergion.githubbackend.domain.utils.types;

import java.net.URI;

public record Github(String id, URI url) {
    @Override
    public String toString() {
        return "{\"id\": %s, \"url\": %s}".formatted(id, url);
    }
}
