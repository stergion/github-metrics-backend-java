package com.stergion.githubbackend.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonObjectMapper {
    private JsonObjectMapper() {
        // Prevent instantiation
    }

    public static ObjectMapper create() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }
}
