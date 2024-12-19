package com.stergion.githubbackend.domain.utils.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;

public record PullRequestReviewComment(String login, Github github, String body) {
    static ObjectMapper mapper = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"login\": %s, \"body\": %s}".formatted(login, body);
        }
    }
}
