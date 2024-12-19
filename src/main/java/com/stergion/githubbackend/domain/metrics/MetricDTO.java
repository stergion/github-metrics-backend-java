package com.stergion.githubbackend.domain.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.validation.constraints.NotNull;

public record MetricDTO(
        @NotNull
        String login,
        @NotNull
        NameWithOwner repository
) {
    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"user\": %s, \"repository\": %s}".formatted(login, repository);
        }
    }
}
