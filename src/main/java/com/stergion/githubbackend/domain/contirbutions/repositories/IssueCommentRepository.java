package com.stergion.githubbackend.domain.contirbutions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;

public interface IssueCommentRepository extends ContributionRepository<IssueComment, IssueCommentSearchCriteria> {}
