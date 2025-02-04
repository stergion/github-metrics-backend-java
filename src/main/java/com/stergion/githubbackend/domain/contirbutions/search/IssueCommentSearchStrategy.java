package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueComment;

public interface IssueCommentSearchStrategy
        extends ContributionSearchStrategy<IssueComment, IssueCommentSearchCriteria> {
}
