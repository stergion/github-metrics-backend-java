package com.stergion.githubbackend.external.githubservice.utils;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.client.SseEvent;

@ApplicationScoped
public class SseEventTransformer {
    private final ErrorResponseMapper errorResponseMapper;
    private final SuccessResponseMapper successResponseMapper;

    public SseEventTransformer(ErrorResponseMapper errorResponseMapper,
                               SuccessResponseMapper successResponseMapper) {
        this.errorResponseMapper = errorResponseMapper;
        this.successResponseMapper = successResponseMapper;
    }


    public <T> T transform(SseEvent<String> event, Class<T> eventType) {
        String eventName = event.name();
        String data = event.data();

        if ("error".equals(eventName)) {
            throw errorResponseMapper.toThrowable(data);
        }

        if ("success".equals(eventName)) {
            return successResponseMapper.fromJson(data, eventType);
        }

        throw new IllegalStateException("Unknown event type: " + eventName);
    }

}
