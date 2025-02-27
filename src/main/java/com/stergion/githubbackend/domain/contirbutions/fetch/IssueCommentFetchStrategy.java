package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.ContributionClient;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class IssueCommentFetchStrategy extends BaseFetchStrategy<IssueComment> {
    protected IssueCommentFetchStrategy() {
        super(null);
    }

    @Inject
    public IssueCommentFetchStrategy(ContributionClient client) {
        super(client);
    }

    @Override
    protected Multi<IssueComment> doFetch(FetchParams params) {
        return client.getIssueComments(
                params.login(),
                params.from(),
                params.to()
                                      );
    }

    @Override
    protected Multi<List<IssueComment>> doFetchBatched(FetchParams params,
                                                       BatchProcessorConfig config) {
        return client.getIssueCommentsBatched(
                params.login(),
                params.from(),
                params.to(),
                config);
    }
}
