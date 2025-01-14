package com.stergion.githubbackend.common.batch;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class BatchProcessorTest {
    private BatchProcessor processor;
    private BatchProcessorConfig config;

    @BeforeEach
    void setup() {
        config = BatchProcessorConfig.defaultConfig();
        processor = new BatchProcessor(config);
    }

    @Test
    void shouldProcessBatchWithinTimeout() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        Multi<Optional<Integer>> stream = Multi.createFrom().iterable(numbers).map(Optional::of);

        BatchResult<Integer> result = processor.processBatch(stream);

        result.toMulti()
              .subscribe().with(batch -> {
                  assertFalse(batch.isEmpty());
                  assertTrue(batch.size() <= config.getBatchSize());
                  assertEquals(numbers, batch);
              });
    }

    @Test
    void shouldHandleEmptyStream() {
        Multi<Optional<String>> emptyStream = Multi.createFrom().empty();

        BatchResult<String> result = processor.processBatch(emptyStream);

        result.toMulti()
              .subscribe().with(batch -> assertTrue(batch.isEmpty()));
    }

    @Test
    void shouldFilterNullValues() {
        List<String> items = Arrays.asList("a", null, "b", null, "c");
        Multi<Optional<String>> stream = Multi.createFrom().iterable(items).map(Optional::of);

        BatchResult<String> result = processor.processBatch(stream);

        result.toMulti()
              .subscribe().with(batch -> {
                  assertFalse(batch.contains(null));
                  assertEquals(List.of("a", "b", "c"), batch);
              });
    }

    @Test
    @DisplayName("Should recover from runtime errors")
    void testRuntimeErrorHandling() {
        BatchProcessorConfig config = BatchProcessorConfig.defaultConfig();
        BatchProcessor processor = new BatchProcessor(config);

        // Create a stream that will throw an exception after a few items
        Multi<Optional<Integer>> source = Multi.createFrom().items(1, 2, 3)
                                               .map(Optional::of)
                                               .map(Unchecked.function(i -> {
                                                   if (i.get() == 3) {
                                                       throw new RuntimeException(
                                                               "Simulated error");
                                                   }
                                                   return i;
                                               }));

        BatchResult<Integer> result = processor.processBatch(source);

        AssertSubscriber<List<Integer>> subscriber = result.toMulti()
                                                           .subscribe()
                                                           .withSubscriber(
                                                                   AssertSubscriber.create(10));

        subscriber
                .awaitItems(1)
                .assertItems(List.of(1, 2))
                .assertCompleted(); // Should complete due to recoverWithCompletion
    }
}
