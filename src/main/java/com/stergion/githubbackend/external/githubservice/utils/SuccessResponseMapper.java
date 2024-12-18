package com.stergion.githubbackend.external.githubservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SuccessResponseMapper{
    private final ObjectMapper objectMapper;

    public SuccessResponseMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T fromJson(String jsonResponse, Class<T> eventType){
        try {
            return objectMapper.readValue(jsonResponse, eventType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
