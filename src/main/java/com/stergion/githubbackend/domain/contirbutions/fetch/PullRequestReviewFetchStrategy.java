package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestDTO;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestReviewDTO;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.ContributionClient;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PullRequestReviewFetchStrategy extends BaseFetchStrategy<PullRequestReviewDTO> {

    @Inject
    public PullRequestReviewFetchStrategy(ContributionClient client) {
        super(client);
    }

    @Override
    protected Multi<PullRequestReviewDTO> doFetch(FetchParams params) {
        return client.getPullRequestReviews(
                params.login(),
                params.from(),
                params.to()
                               );
    }

    @Override
    protected Multi<List<PullRequestReviewDTO>> doFetchBatched(FetchParams params,
                                                   BatchProcessorConfig config) {
        return client.getPullRequestReviewsBatched(
                params.login(),
                params.from(),
                params.to(),
                config);
    }
}
