package com.stergion.githubbackend.common.batch;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;

import java.util.Optional;

public class BatchProcessor {
    private static final Logger log = Logger.getLogger(BatchProcessor.class);

    private final BatchProcessorConfig config;

    public BatchProcessor(BatchProcessorConfig config) {
        this.config = config;
    }

    public <T> BatchResult<T> processBatch(Multi<Optional<T>> stream) {
        var result = stream.ifNoItem().after(config.getInactivityTimeout()).recoverWithCompletion()       // 1. Detect inactivity
                           .onFailure().invoke(t -> {
                                       log.warn("Batch processing failed. Recovering with completion");
                                       log.warn("Cause", t);
                                   })
                           .onFailure().recoverWithCompletion()
                           .filter(Optional::isPresent)
                           .map(Optional::get)          // 2. Filter heartbeats
                           .onOverflow().buffer(config.getBufferSize())  // 4. Backpressure
                           .group().intoLists().of(config.getBatchSize(), config.getBatchTimeout());// 5. Batch processing
        return new BatchResult<>(result);
    }
}
