package com.stergion.githubbackend.domain.utils.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;

public record File(
        String fileName,
        String baseName,
        String extension,
        String path,
        String status,
        int additions,
        int deletions,
        int changes,
        String patch
) {
    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"fileName\": %s, \"additions\": %s, \"deletions\": %s}".formatted(fileName,
                    additions, deletions);
        }
    }
}
