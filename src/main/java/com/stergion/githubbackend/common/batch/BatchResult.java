package com.stergion.githubbackend.common.batch;

import io.smallrye.mutiny.Multi;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BatchResult<T> {
    private final Multi<List<T>> result;

    BatchResult(Multi<List<T>> result) {
        this.result = result;
    }

    public <R> BatchResult<R> transform(Function<T, R> transformer) {
        Multi<List<R>> transformed = result.map(list -> list.stream()
                                                            .map(transformer)
                                                            .toList());
        return new BatchResult<>(transformed);
    }

    public BatchResult<T> peek(Consumer<? super T> consumer) {
        Multi<List<T>> peeked = result.map(list -> {
            list.forEach(consumer);
            return list;
        });
        return new BatchResult<>(peeked);
    }

    public Multi<List<T>> toMulti() {
        return result;
    }

}
