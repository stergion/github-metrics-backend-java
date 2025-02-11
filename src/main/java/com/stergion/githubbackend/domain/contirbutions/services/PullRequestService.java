package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.PullRequestFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.PullRequestMapper;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.PullRequestSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.MongoPullRequestRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PullRequestService extends ContributionService<PullRequest, PullRequestEntity> {
    @Inject
    PullRequestMapper pullRequestMapper;
    @Inject
    PullRequestSearchStrategy searchStrategy;

    @Inject
    public PullRequestService(MongoPullRequestRepository pullRequestRepository,
                              PullRequestFetchStrategy fetchStrategy,
                              PullRequestMapper pullRequestMapper) {
        super(pullRequestRepository, fetchStrategy);
        this.pullRequestMapper = pullRequestMapper;
    }

    @Override
    protected PullRequestEntity mapDomainToEntity(PullRequest pullRequest, ObjectId userId, ObjectId repoId) {
        return pullRequestMapper.toEntity(pullRequest, userId, repoId);
    }

    @Override
    protected PullRequest mapEntityToDomain(PullRequestEntity entity) {
        return pullRequestMapper.toDomain(entity);
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
