package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.ContributionClient;
import io.smallrye.mutiny.Multi;

import java.util.List;

public abstract class BaseFetchStrategy<T> implements FetchStrategy<T> {
    protected final ContributionClient client;

    protected BaseFetchStrategy(ContributionClient client) {
        this.client = client;
    }

    @Override
    public Multi<T> fetch(FetchParams params) {
        validateParams(params);
        return doFetch(params)
                .onFailure().invoke(this::handleError);
    }

    @Override
    public Multi<List<T>> fetchBatched(FetchParams params, BatchProcessorConfig config) {
        validateParams(params);
        return doFetchBatched(params, config)
                .onFailure().invoke(this::handleError);
    }

    protected abstract Multi<T> doFetch(FetchParams params);

    protected abstract Multi<List<T>> doFetchBatched(FetchParams params,
                                                     BatchProcessorConfig config);

    protected void handleError(Throwable error) {
        throw new FetchException("Unexpected error occurred", error);
    }
}
