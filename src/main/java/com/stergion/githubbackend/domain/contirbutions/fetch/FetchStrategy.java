package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import io.smallrye.mutiny.Multi;

import java.util.List;

public interface FetchStrategy<T> {
    Multi<T> fetch(FetchParams params);
    Multi<List<T>> fetchBatched(FetchParams params, BatchProcessorConfig config);
    default void validateParams(FetchParams params) {
        params.validate();
    }
}