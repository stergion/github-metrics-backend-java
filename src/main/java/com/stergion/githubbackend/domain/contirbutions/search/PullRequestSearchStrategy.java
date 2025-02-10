package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestEntity;

public interface PullRequestSearchStrategy
        extends ContributionSearchStrategy<PullRequestEntity, PullRequestSearchCriteria> {
}
