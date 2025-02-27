package com.stergion.githubbackend.domain.contirbutions.search;

import com.stergion.githubbackend.domain.contirbutions.search.criteria.BaseSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.ContributionEntity;
import io.smallrye.mutiny.Uni;

public interface ContributionSearchStrategy<T extends ContributionEntity, U extends BaseSearchCriteria<?, ?>> {
    /**
     * Performs a search based on the provided criteria
     *
     * @param criteria the search criteria
     * @return a Uni containing the paged response
     */
    Uni<PagedResponse<T>> search(U criteria);
}