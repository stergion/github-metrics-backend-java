package com.stergion.githubbackend.domain.contirbutions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.PullRequest;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;

public interface PullRequestRepository extends ContributionRepository<PullRequest, PullRequestSearchCriteria> {}
