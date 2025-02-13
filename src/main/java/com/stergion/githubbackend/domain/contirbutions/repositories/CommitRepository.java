package com.stergion.githubbackend.domain.contirbutions.repositories;

import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;

public interface CommitRepository extends ContributionRepository<Commit, CommitSearchCriteria> {}
