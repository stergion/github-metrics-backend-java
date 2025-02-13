package com.stergion.githubbackend.domain.contirbutions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;

public interface PullRequestReviewRepository extends ContributionRepository<PullRequestReview, PullRequestReviewSearchCriteria> {}
