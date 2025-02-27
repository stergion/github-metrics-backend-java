package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.PullRequestReviewFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.domain.contirbutions.repositories.PullRequestReviewRepository;
import com.stergion.githubbackend.domain.contirbutions.search.PullRequestReviewSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.PullRequestReviewMapper;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PullRequestReviewService
        extends ContributionService<PullRequestReview, PullRequestReviewSearchCriteria> {
    @Inject
    PullRequestReviewMapper pullRequestReviewMapper;
    @Inject
    PullRequestReviewSearchStrategy searchStrategy;

    @Inject
    public PullRequestReviewService(PullRequestReviewRepository pullRequestReviewRepository,
                                    PullRequestReviewFetchStrategy fetchStrategy) {
        super(pullRequestReviewRepository, fetchStrategy);
    }


    public Multi<List<PullRequestReview>> fetchAndCreatePullRequestReviews(String login,
                                                                           LocalDateTime from,
                                                                           LocalDateTime to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();
        return fetchAndCreate(params);
    }

//    public Uni<PagedResponse<PullRequestReview>> search(
//            PullRequestReviewSearchCriteria criteria) {
//        return searchStrategy.search(criteria)
//                             .map(response -> PagedResponse.map(response,
//                                     pullRequestReviewMapper::toDomain));
//    }
}
