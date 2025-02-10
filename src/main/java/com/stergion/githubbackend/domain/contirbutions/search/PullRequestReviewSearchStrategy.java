package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestReviewEntity;

public interface PullRequestReviewSearchStrategy
        extends ContributionSearchStrategy<PullRequestReviewEntity, PullRequestReviewSearchCriteria> {
}
