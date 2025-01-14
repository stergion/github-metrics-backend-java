package com.stergion.githubbackend.infrastructure.external.githubservice.utils;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.client.SseEvent;

import java.util.Optional;

@ApplicationScoped
public class SseEventTransformer {
    private final ErrorResponseMapper errorResponseMapper;
    private final SuccessResponseMapper successResponseMapper;

    public SseEventTransformer(ErrorResponseMapper errorResponseMapper,
                               SuccessResponseMapper successResponseMapper) {
        this.errorResponseMapper = errorResponseMapper;
        this.successResponseMapper = successResponseMapper;
    }


    public <T> Optional<T> transform(SseEvent<String> event, Class<T> eventType) {
        String eventName = event.name();
        String data = event.data();

        if ("error".equals(eventName)) {
            throw errorResponseMapper.toThrowable(data);
        }

        if ("success".equals(eventName)) {
            var result = successResponseMapper.fromJson(data, eventType);
            return Optional.of(result);
        }

        if ("heartbeat".equals(eventName)) {
            return Optional.empty();
        }

        throw new IllegalStateException("Unknown event type: " + eventName);
    }

}
