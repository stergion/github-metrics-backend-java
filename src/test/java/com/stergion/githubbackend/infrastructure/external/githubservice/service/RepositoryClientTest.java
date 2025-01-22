package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.domain.repositories.RepositoryDTO;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions.InternalServerException;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions.NotGithubUserException;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.exceptions.RepositoryNotFoundException;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers.RepositoryGHMapper;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.RepositoryGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.utils.SseEventTransformer;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.TimeoutException;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.client.SseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("RepositoryClient Tests")
class RepositoryClientTest extends BaseGitHubServiceTest {

    @InjectMock
    @RestClient
    GitHubServiceClient gitHubServiceClient;

    @InjectMock
    SseEventTransformer transformer;

    @InjectMock
    RepositoryGHMapper mapper;

    @Inject
    RepositoryClient repositoryClient;

    @BeforeEach
    void setUp() {
        reset(gitHubServiceClient, transformer, mapper);
    }

    @Nested
    @DisplayName("getRepositoryInfo")
    class GetRepositoryInfo {
        private final RepositoryBuilder repoBuilder = new RepositoryBuilder();

        @Test
        @DisplayName("Should successfully retrieve repository information")
        void shouldRetrieveRepositoryInfo() {
            // Arrange
            RepositoryGH mockRepo = repoBuilder.withOwner(TEST_OWNER)
                                               .withName(TEST_NAME)
                                               .build();
            RepositoryDTO expectedDTO = repoBuilder.buildDTO();

            when(gitHubServiceClient.getRepository(TEST_OWNER, TEST_NAME)).thenReturn(mockRepo);
            when(mapper.toDTO(mockRepo)).thenReturn(expectedDTO);

            // Act
            RepositoryDTO result = repositoryClient.getRepositoryInfo(TEST_OWNER, TEST_NAME);

            // Assert
            assertAll(
                    "Repository validation",
                    () -> assertNotNull(result, "Result should not be null"),
                    () -> assertEquals(expectedDTO.owner(), result.owner(), "Owner should match"),
                    () -> assertEquals(expectedDTO.name(), result.name(), "Name should match"),
                    () -> assertEquals(expectedDTO.stargazerCount(), result.stargazerCount(),
                            "Star count should match")
                     );
            verify(gitHubServiceClient).getRepository(TEST_OWNER, TEST_NAME);
            verify(mapper).toDTO(mockRepo);
        }

        @Test
        @DisplayName("Should throw RepositoryNotFoundException for non-existent repository")
        void shouldThrowRepositoryNotFoundException() {
            // Arrange
            when(gitHubServiceClient.getRepository(TEST_OWNER, TEST_NAME))
                    .thenThrow(new RepositoryNotFoundException("Repository not found"));

            // Act & Assert
            assertThrows(RepositoryNotFoundException.class,
                    () -> repositoryClient.getRepositoryInfo(TEST_OWNER, TEST_NAME));
        }

        @Test
        @DisplayName("Should handle null repository name")
        void shouldHandleNullRepositoryName() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> repositoryClient.getRepositoryInfo(TEST_OWNER, null));
        }
    }

    @Nested
    @DisplayName("getRepositoriesCommittedTo")
    class GetRepositoriesCommittedTo {
        private final RepositoryBuilder repoBuilder = new RepositoryBuilder();

        @Test
        @DisplayName("Should successfully stream repositories with mixed events")
        void shouldStreamRepositoriesWithMixedEvents() {
            int dataEventCount = 5;
            List<RepositoryGH> mockRepos = repoBuilder.buildList(dataEventCount);
            List<RepositoryDTO> expectedDTOs = repoBuilder.buildDTOList(dataEventCount);
            List<SseEvent<String>> events = new ArrayList<>();

            for (int i = 0; i < dataEventCount; i++) {
                SseEvent<String> event = mock(SseEvent.class);
                when(event.name()).thenReturn("success");
                when(event.data()).thenReturn(String.format("{\"id\":\"repo-%d\"}", i));
                when(transformer.transform(event, RepositoryGH.class)).thenReturn(Optional.of(mockRepos.get(i)));
                when(mapper.toDTO(mockRepos.get(i))).thenReturn(expectedDTOs.get(i));
                events.add(event);

                if (i < dataEventCount - 1) {
                    SseEvent<String> heartbeat = mock(SseEvent.class);
                    when(heartbeat.name()).thenReturn("heartbeat");
                    when(transformer.transform(heartbeat, RepositoryGH.class)).thenReturn(Optional.empty());
                    events.add(heartbeat);
                }
            }

            when(gitHubServiceClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE))
                    .thenReturn(Multi.createFrom().iterable(events));

            List<RepositoryDTO> result = repositoryClient
                    .getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE)
                    .collect().asList()
                    .await().indefinitely();

            assertEquals(dataEventCount, result.size());
            assertEquals(expectedDTOs, result);
            verify(transformer, times(events.size())).transform(any(), eq(RepositoryGH.class));
            verify(mapper, times(dataEventCount)).toDTO(any());
        }

        @Test
        @DisplayName("Should throw NotGithubUserException")
        void shouldHandleNotGithubUserException() {
            SseEvent<String> errorEvent = createErrorEvent(
                    "{\"name\":\"NotGithubUser\",\"message\":\"User not found\"}");
            // Arrange
            when(gitHubServiceClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE))
                    .thenReturn(Multi.createFrom().item(errorEvent));
            when(transformer.transform(any(), eq(RepositoryGH.class)))
                    .thenThrow(new NotGithubUserException("User not found"));

            // Act & Assert
            assertThrows(NotGithubUserException.class, () ->
                    repositoryClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE)
                                    .collect().asList()
                                    .await().indefinitely());
        }

        @Test
        @DisplayName("Should handle timeout")
        void shouldHandleTimeout() {
            // Arrange
            when(gitHubServiceClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE))
                    .thenReturn(Multi.createFrom().nothing());

            // Act & Assert
            assertThrows(TimeoutException.class, () ->
                    repositoryClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE)
                                    .collect().asList()
                                    .await().atMost(java.time.Duration.ofMillis(100)));
        }
    }

    @Nested
    @DisplayName("getRepositoriesCommittedToBatched")
    class GetRepositoriesCommittedToBatched {
        private final RepositoryBuilder repoBuilder = new RepositoryBuilder();

        @Test
        @DisplayName("Should handle backpressure with large dataset")
        void shouldHandleBackpressureWithLargeDataset() {
            // Arrange
            int totalEvents = LARGE_BATCH_CONFIG.getBatchSize() * 3; // Multiple batches
            List<SseEvent<String>> events = createMockSseEvents(totalEvents,
                    i -> String.format("{\"id\":\"repo-%d\"}", i));
            List<RepositoryGH> mockRepos = repoBuilder.buildList(totalEvents);
            List<RepositoryDTO> expectedDTOs = repoBuilder.buildDTOList(totalEvents);

            when(gitHubServiceClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE))
                    .thenReturn(Multi.createFrom().items(events.stream()));

            for (int i = 0; i < totalEvents; i++) {
                when(transformer.transform(events.get(i), RepositoryGH.class))
                        .thenReturn(Optional.of(mockRepos.get(i)));
                when(mapper.toDTO(mockRepos.get(i)))
                        .thenReturn(expectedDTOs.get(i));
            }

            // Act
            List<List<RepositoryDTO>> result = repositoryClient
                    .getRepositoriesCommittedToBatched(TEST_LOGIN, FROM_DATE, TO_DATE,
                            LARGE_BATCH_CONFIG)
                    .collect().asList()
                    .await().indefinitely();

            // Assert
            int expectedBatches = (int) Math.ceil(
                    (double) totalEvents / LARGE_BATCH_CONFIG.getBatchSize());
            assertEquals(expectedBatches, result.size());
            assertEquals(LARGE_BATCH_CONFIG.getBatchSize(), result.getFirst().size());
            verify(transformer, times(totalEvents)).transform(any(), eq(RepositoryGH.class));
            verify(mapper, times(totalEvents)).toDTO(any());
        }

        @Test
        @DisplayName("Should handle error in middle of batch")
        void shouldHandleErrorInMiddleOfBatch() {
            // Arrange
            int eventsBeforeError = 75;
            List<SseEvent<String>> events = new ArrayList<>(createMockSseEvents(eventsBeforeError,
                    i -> String.format("{\"id\":\"repo-%d\"}", i)));
            events.add(createErrorEvent(
                    "{\"statusCode\":500,\"name\":\"InternalServerError\",\"message\":\"Server error\"}"));

            when(gitHubServiceClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE))
                    .thenReturn(Multi.createFrom().items(events.stream()));

            List<RepositoryGH> mockRepos = repoBuilder.buildList(eventsBeforeError);
            List<RepositoryDTO> expectedDTOs = repoBuilder.buildDTOList(eventsBeforeError);

            for (int i = 0; i < eventsBeforeError; i++) {
                when(transformer.transform(events.get(i), RepositoryGH.class))
                        .thenReturn(Optional.of(mockRepos.get(i)));
                when(mapper.toDTO(mockRepos.get(i)))
                        .thenReturn(expectedDTOs.get(i));
            }

            when(transformer.transform(events.get(eventsBeforeError), RepositoryGH.class))
                    .thenThrow(new InternalServerException("Server error"));

            // Act & Assert
            Multi<List<RepositoryDTO>> batchedResult = repositoryClient
                    .getRepositoriesCommittedToBatched(TEST_LOGIN, FROM_DATE, TO_DATE, SMALL_BATCH_CONFIG);

            List<List<RepositoryDTO>> result = batchedResult
                    .collect().asList()
                    .await().asOptional().indefinitely().get();

            // Verify we got the first complete batch
            assertEquals(2, result.size());
            assertEquals(50, result.get(0).size());
            assertEquals(25, result.get(1).size());

            // Verify all events up to the error were processed
            verify(transformer, times(eventsBeforeError+1)).transform(any(), eq(RepositoryGH.class));
            verify(mapper, times(eventsBeforeError)).toDTO(any());

            // Verify error handling
            verify(transformer).transform(eq(events.get(eventsBeforeError)), eq(RepositoryGH.class));
        }
        
        @Test
        @DisplayName("Should handle empty result set")
        void shouldHandleEmptyResultSet() {
            // Arrange
            when(gitHubServiceClient.getRepositoriesCommittedTo(TEST_LOGIN, FROM_DATE, TO_DATE))
                    .thenReturn(Multi.createFrom().empty());

            // Act
            List<List<RepositoryDTO>> result = repositoryClient
                    .getRepositoriesCommittedToBatched(TEST_LOGIN, FROM_DATE, TO_DATE,
                            SMALL_BATCH_CONFIG)
                    .collect().asList()
                    .await().indefinitely();

            // Assert
            assertTrue(result.isEmpty());
            verify(transformer, never()).transform(any(), any());
            verify(mapper, never()).toDTO(any());
        }
    }
}