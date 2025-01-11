package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.IssueDTO;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestDTO;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.ContributionClient;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PullRequestFetchStrategy extends BaseFetchStrategy<PullRequestDTO> {

    @Inject
    public PullRequestFetchStrategy(ContributionClient client) {
        super(client);
    }

    @Override
    protected Multi<PullRequestDTO> doFetch(FetchParams params) {
        return client.getPullRequests(
                params.login(),
                params.from(),
                params.to()
                               );
    }

    @Override
    protected Multi<List<PullRequestDTO>> doFetchBatched(FetchParams params,
                                                   BatchProcessorConfig config) {
        return client.getPullRequestsBatched(
                params.login(),
                params.from(),
                params.to(),
                config);
    }
}
