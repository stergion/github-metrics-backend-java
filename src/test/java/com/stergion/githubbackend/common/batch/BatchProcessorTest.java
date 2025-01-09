package com.stergion.githubbackend.common.batch;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
        Multi<Integer> stream = Multi.createFrom().iterable(numbers);

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
        Multi<String> emptyStream = Multi.createFrom().empty();

        BatchResult<String> result = processor.processBatch(emptyStream);

        result.toMulti()
              .subscribe().with(batch -> assertTrue(batch.isEmpty()));
    }

    @Test
    void shouldFilterNullValues() {
        List<String> items = Arrays.asList("a", null, "b", null, "c");
        Multi<String> stream = Multi.createFrom().iterable(items);

        BatchResult<String> result = processor.processBatch(stream);

        result.toMulti()
              .subscribe().with(batch -> {
                  assertFalse(batch.contains(null));
                  assertEquals(List.of("a", "b", "c"), batch);
              });
    }
}
