package com.stergion.githubbackend.common.batch;

import java.time.Duration;

import java.util.Objects;

/**
 * Configuration for batch processing of SSE events.
 * Uses the Builder pattern to create instances with customizable parameters.
 * All instances are immutable once created.
 */
public final class BatchProcessorConfig {
    private final int batchSize;
    private final Duration batchTimeout;
    private final Duration inactivityTimeout;
    private final boolean enableBackpressure;
    private final int maxRetries;
    private final int bufferSize;

    private BatchProcessorConfig(Builder builder) {
        this.batchSize = builder.batchSize;
        this.batchTimeout = builder.batchTimeout;
        this.inactivityTimeout = builder.inactivityTimeout;
        this.enableBackpressure = builder.enableBackpressure;
        this.maxRetries = builder.maxRetries;
        this.bufferSize = builder.bufferSize;
    }

    // Getters
    public int getBatchSize() {
        return batchSize;
    }

    public Duration getBatchTimeout() {
        return batchTimeout;
    }

    public Duration getInactivityTimeout() {
        return inactivityTimeout;
    }

    public boolean isBackpressureEnabled() {
        return enableBackpressure;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    // Factory methods for common configurations
    public static BatchProcessorConfig defaultConfig() {
        return new Builder().build();
    }

    public static BatchProcessorConfig smallBatch() {
        return new Builder()
                .batchSize(50)
                .bufferSize(128)
                .build();
    }

    public static BatchProcessorConfig largeBatch() {
        return new Builder()
                .batchSize(200)
                .bufferSize(512)
                .build();
    }

    public static BatchProcessorConfig noRetry() {
        return new Builder()
                .maxRetries(0)
                .build();
    }

    /**
     * Builder class for creating BatchConfig instances.
     * Provides default values and validation for all parameters.
     */
    public static class Builder {
        // Default values
        private int batchSize = 100;
        private Duration batchTimeout = Duration.ofSeconds(120);
        private Duration inactivityTimeout = Duration.ofSeconds(60);
        private boolean enableBackpressure = true;
        private int maxRetries = 3;
        private int bufferSize = 256;

        public Builder batchSize(int batchSize) {
            validatePositive(batchSize, "Batch size");
            this.batchSize = batchSize;
            return this;
        }

        public Builder batchTimeout(Duration timeout) {
            Objects.requireNonNull(timeout, "Batch timeout cannot be null");
            if (timeout.isNegative() || timeout.isZero()) {
                throw new IllegalArgumentException("Batch timeout must be positive");
            }
            this.batchTimeout = timeout;
            return this;
        }

        public Builder inactivityTimeout(Duration timeout) {
            Objects.requireNonNull(timeout, "Inactivity Timeout cannot be null");
            if (timeout.isNegative() || timeout.isZero()) {
                throw new IllegalArgumentException("Inactivity Timeout must be positive");
            }
            this.inactivityTimeout = timeout;
            return this;
        }

        public Builder enableBackpressure(boolean enableBackpressure) {
            this.enableBackpressure = enableBackpressure;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            validateNonNegative(maxRetries, "Max retries");
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder bufferSize(int bufferSize) {
            validatePositive(bufferSize, "Buffer size");
            if (!isPowerOfTwo(bufferSize)) {
                throw new IllegalArgumentException("Buffer size must be a power of 2");
            }
            this.bufferSize = bufferSize;
            return this;
        }

        public BatchProcessorConfig build() {
            validate();
            return new BatchProcessorConfig(this);
        }

        private void validate() {
            if (batchSize > bufferSize) {
                throw new IllegalStateException(
                        "Batch size (" + batchSize + ") cannot be larger than buffer size (" +
                                bufferSize + ")"
                );
            }
        }

        private void validatePositive(int value, String fieldName) {
            if (value <= 0) {
                throw new IllegalArgumentException(fieldName + " must be positive");
            }
        }

        private void validateNonNegative(int value, String fieldName) {
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative");
            }
        }

        private boolean isPowerOfTwo(int value) {
            return value > 0 && (value & (value - 1)) == 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatchProcessorConfig that = (BatchProcessorConfig) o;
        return batchSize == that.batchSize &&
                enableBackpressure == that.enableBackpressure &&
                maxRetries == that.maxRetries &&
                bufferSize == that.bufferSize &&
                Objects.equals(timeout, that.timeout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchSize, timeout, enableBackpressure, maxRetries, bufferSize);
    }

    @Override
    public String toString() {
        return "BatchConfig{" +
                "batchSize=" + batchSize +
                ", batchTimeout=" + batchTimeout +
                ", inactivityTimeout=" + inactivityTimeout +
                ", enableBackpressure=" + enableBackpressure +
                ", maxRetries=" + maxRetries +
                ", bufferSize=" + bufferSize +
                '}';
    }
}