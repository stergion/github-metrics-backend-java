package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.RepositoryOwner;
import org.jboss.resteasy.reactive.client.SseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseGitHubServiceTest {

    protected static final String TEST_OWNER = "testOwner";
    protected static final String TEST_NAME = "testRepo";
    protected static final String TEST_LOGIN = "testUser";
    protected static final LocalDateTime FROM_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
    protected static final LocalDateTime TO_DATE = LocalDateTime.of(2024, 1, 31, 0, 0);

    // Common batch processing configurations
    protected static final BatchProcessorConfig SMALL_BATCH_CONFIG =
            BatchProcessorConfig.smallBatch();
    protected static final BatchProcessorConfig LARGE_BATCH_CONFIG =
            BatchProcessorConfig.largeBatch();

    @BeforeEach
    abstract void setUp();

    // Helper method to create SSE events
    protected List<SseEvent<String>> createMockSseEvents(int count,
                                                         Function<Integer, String> dataProvider) {
        List<SseEvent<String>> events = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SseEvent<String> event = mock(SseEvent.class);
            when(event.data()).thenReturn(dataProvider.apply(i));
            when(event.name()).thenReturn("success");
            events.add(event);
        }
        return events;
    }

    // Helper method to create heartbeat events
    protected List<SseEvent<String>> createHeartbeatEvents(int count) {
        List<SseEvent<String>> events = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SseEvent<String> event = mock(SseEvent.class);
            when(event.name()).thenReturn("heartbeat");
            events.add(event);
        }
        return events;
    }

    // Helper method to create error events
    protected SseEvent<String> createErrorEvent(String errorMessage) {
        SseEvent<String> event = mock(SseEvent.class);
        when(event.name()).thenReturn("error");
        when(event.data()).thenReturn(errorMessage);
        return event;
    }

    // Helper method to create GitHub reference objects
    protected Github createGithubRef(String id) {
        try {
            return new Github(id, new URI("https://github.com/test/" + id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Github reference", e);
        }
    }

    // Helper method to create RepositoryOwner objects
    protected RepositoryOwner createRepositoryOwner(String login) {
        return new RepositoryOwner(login);
    }

    // Helper method to create ArgumentCaptor for SseEvent
    protected ArgumentCaptor<SseEvent<String>> createSseEventCaptor() {
        return ArgumentCaptor.forClass(SseEvent.class);
    }

    // Helper method to mix regular and heartbeat events
    protected List<SseEvent<String>> mixEvents(List<SseEvent<String>> regularEvents,
                                               int heartbeatCount) {
        List<SseEvent<String>> mixed = new ArrayList<>();
        List<SseEvent<String>> heartbeats = createHeartbeatEvents(heartbeatCount);

        int regularIndex = 0;
        int heartbeatIndex = 0;

        while (regularIndex < regularEvents.size() || heartbeatIndex < heartbeats.size()) {
            // Add two regular events followed by a heartbeat
            for (int i = 0; i < 2 && regularIndex < regularEvents.size(); i++) {
                mixed.add(regularEvents.get(regularIndex++));
            }
            if (heartbeatIndex < heartbeats.size()) {
                mixed.add(heartbeats.get(heartbeatIndex++));
            }
        }

        return mixed;
    }

    // Helper method to create URI safely
    protected URI createUri(String path) {
        try {
            return new URI("https://github.com/" + path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create URI", e);
        }
    }
}