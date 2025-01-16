package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.CommitDTO;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.ContributionClient;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CommitFetchStrategy extends BaseFetchStrategy<CommitDTO> {

    protected CommitFetchStrategy() {
        super(null);
    }

    @Inject
    public CommitFetchStrategy(ContributionClient client) {
        super(client);
    }

    @Override
    public void validateParams(FetchParams params) {
        params.validateForCommits(); // Uses commit-specific validation
    }

    @Override
    protected Multi<CommitDTO> doFetch(FetchParams params) {
        var repo = params.nameWithOwner()
                         .orElseThrow(() -> new ValidationException(
                                 "Repository is required for commits"));

        return client.getCommits(
                params.login(),
                repo.owner(),
                repo.name(),
                params.from(),
                params.to()
                                );
    }

    @Override
    protected Multi<List<CommitDTO>> doFetchBatched(FetchParams params,
                                                    BatchProcessorConfig config) {
        var repo = params.nameWithOwner()
                         .orElseThrow(() -> new ValidationException(
                                 "Repository is required for commits"));

        return client.getCommitsBatched(
                params.login(),
                repo.owner(),
                repo.name(),
                params.from(),
                params.to(),
                config
                                       );
    }
}