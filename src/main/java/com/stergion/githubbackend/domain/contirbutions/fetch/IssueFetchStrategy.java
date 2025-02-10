package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.models.IssueDTO;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.ContributionClient;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class IssueFetchStrategy extends BaseFetchStrategy<IssueDTO> {

    protected IssueFetchStrategy() {
        super(null);
    }

    @Inject
    public IssueFetchStrategy(ContributionClient client) {
        super(client);
    }

    @Override
    protected Multi<IssueDTO> doFetch(FetchParams params) {
        return client.getIssues(
                params.login(),
                params.from(),
                params.to()
                               );
    }

    @Override
    protected Multi<List<IssueDTO>> doFetchBatched(FetchParams params,
                                                   BatchProcessorConfig config) {
        return client.getIssuesBatched(
                params.login(),
                params.from(),
                params.to(),
                config);
    }
}
