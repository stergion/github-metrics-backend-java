package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.CommitEntity;

public interface CommitSearchStrategy
        extends ContributionSearchStrategy<CommitEntity, CommitSearchCriteria> {
}
