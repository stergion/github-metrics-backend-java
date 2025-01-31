package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.Issue;

public interface IssueSearchStrategy
        extends ContributionSearchStrategy<Issue, IssueSearchCriteria> {
}
