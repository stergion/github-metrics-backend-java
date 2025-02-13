package com.stergion.githubbackend.domain.contirbutions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;

public interface IssueRepository extends ContributionRepository<Issue, IssueSearchCriteria> {}
