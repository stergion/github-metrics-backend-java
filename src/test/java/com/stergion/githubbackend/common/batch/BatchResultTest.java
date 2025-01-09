package com.stergion.githubbackend.common.batch;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BatchResultTest {

    @Test
    void shouldTransformBatchItems() {
        Multi<List<Integer>> numbers = Multi.createFrom().item(List.of(1, 2, 3));
        BatchResult<Integer> batchResult = new BatchResult<>(numbers);

        BatchResult<String> transformed = batchResult.transform(num -> "Number: " + num);

        transformed.toMulti()
                   .subscribe().with(batch -> {
                       assertEquals(3, batch.size());
                       assertEquals("Number: 1", batch.get(0));
                       assertEquals("Number: 2", batch.get(1));
                       assertEquals("Number: 3", batch.get(2));
                   });
    }

    @Test
    void shouldPeekBatchItems() {
        AtomicInteger sum = new AtomicInteger(0);
        Multi<List<Integer>> numbers = Multi.createFrom().item(List.of(1, 2, 3));
        BatchResult<Integer> batchResult = new BatchResult<>(numbers);

        BatchResult<Integer> peeked = batchResult.peek(sum::addAndGet);

        peeked.toMulti()
              .subscribe().with(batch -> {
                  assertEquals(6, sum.get());
                  assertEquals(List.of(1, 2, 3), batch);
              });
    }

    @Test
    void shouldConvertToMultiCorrectly() {
        List<String> items = List.of("a", "b", "c");
        Multi<List<String>> multi = Multi.createFrom().item(items);
        BatchResult<String> batchResult = new BatchResult<>(multi);

        Multi<List<String>> result = batchResult.toMulti();

        result.subscribe().with(batch -> assertEquals(items, batch));
    }
}
