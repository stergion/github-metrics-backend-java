package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.Commit;

public interface CommitSearchStrategy
        extends ContributionSearchStrategy<Commit, CommitSearchCriteria> {
}
