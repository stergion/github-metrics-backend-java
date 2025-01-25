package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.PullRequest;

public interface PullRequestSearchStrategy
        extends ContributionSearchStrategy<PullRequest, PullRequestSearchCriteria> {
}
