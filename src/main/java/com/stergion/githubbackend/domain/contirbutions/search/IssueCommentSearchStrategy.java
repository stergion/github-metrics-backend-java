package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;

public interface IssueCommentSearchStrategy
        extends ContributionSearchStrategy<IssueCommentEntity, IssueCommentSearchCriteria> {
}
