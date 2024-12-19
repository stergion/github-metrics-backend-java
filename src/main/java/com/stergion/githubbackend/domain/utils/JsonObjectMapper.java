package com.stergion.githubbackend.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonObjectMapper {
    private JsonObjectMapper() {
        // Prevent instantiation
    }

    public static ObjectMapper create() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }
}
