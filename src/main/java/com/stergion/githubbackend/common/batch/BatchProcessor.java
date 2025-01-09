package com.stergion.githubbackend.common.batch;

import io.smallrye.mutiny.Multi;

import java.util.Objects;

public class BatchProcessor {
    private final BatchProcessorConfig config;

    public BatchProcessor(BatchProcessorConfig config) {
        this.config = config;
    }

    public <T> BatchResult<T> processBatch(Multi<T> stream) {
        var result = stream.ifNoItem().after(config.getTimeout()).recoverWithCompletion()       // 1. Detect inactivity
                     .filter(Objects::nonNull)           // 2. Filter heartbeats
                     .onOverflow().buffer(config.getBufferSize())  // 4. Backpressure
                     .group().intoLists().of(config.getBatchSize(), config.getTimeout())// 5. Batch processing
                     .onFailure().recoverWithCompletion();
        return new BatchResult<>(result);
    }
}
