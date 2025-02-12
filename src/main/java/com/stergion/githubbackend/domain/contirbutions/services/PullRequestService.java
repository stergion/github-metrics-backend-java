package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.PullRequestFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.PullRequestMapper;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.PullRequestSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PullRequestService extends ContributionService<PullRequest, PullRequestEntity> {
    @Inject
    PullRequestMapper pullRequestMapper;
    @Inject
    PullRequestSearchStrategy searchStrategy;

    @Inject
    public PullRequestService(PullRequestRepository pullRequestRepository,
                              PullRequestFetchStrategy fetchStrategy,
                              PullRequestMapper pullRequestMapper) {
        super(pullRequestRepository, fetchStrategy);
        this.pullRequestMapper = pullRequestMapper;
    }


    public Multi<List<PullRequest>> fetchAndCreatePullRequests(String login,
                                                               LocalDateTime from,
                                                               LocalDateTime to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }

    public Uni<PagedResponse<PullRequest>> search(PullRequestSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     pullRequestMapper::toDomain));
    }
}
