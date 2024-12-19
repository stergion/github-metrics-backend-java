package com.stergion.githubbackend.domain.utils.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;

public record CommitComment(
        String author,
        String publishedAt,
        int position,
        String reactionsCount,
        String body
) {
    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"author\": %s, \"publishedAt\": %s}".formatted(author, publishedAt);
        }
    }
}
