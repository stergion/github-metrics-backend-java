package com.stergion.githubbackend.common.batch;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class BatchProcessorConfigTest {

    @Test
    void defaultConfigShouldHaveCorrectValues() {
        BatchProcessorConfig config = BatchProcessorConfig.defaultConfig();

        assertEquals(100, config.getBatchSize());
        assertEquals(Duration.ofSeconds(120), config.getBatchTimeout());
        assertEquals(Duration.ofSeconds(60), config.getInactivityTimeout());
        assertTrue(config.isBackpressureEnabled());
        assertEquals(3, config.getMaxRetries());
        assertEquals(256, config.getBufferSize());
    }

    @Test
    void smallBatchConfigShouldHaveReducedSizes() {
        BatchProcessorConfig config = BatchProcessorConfig.smallBatch();

        assertEquals(50, config.getBatchSize());
        assertEquals(128, config.getBufferSize());
    }

    @Test
    void builderShouldValidateBufferSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new BatchProcessorConfig.Builder().bufferSize(100).build(),
                "Buffer size must be a power of 2");
    }

    @Test
    void builderShouldValidateBatchSizeAgainstBufferSize() {
        assertThrows(IllegalStateException.class,
                () -> new BatchProcessorConfig.Builder()
                        .batchSize(300)
                        .bufferSize(256)
                        .build(),
                "Batch size cannot be larger than buffer size");
    }
}