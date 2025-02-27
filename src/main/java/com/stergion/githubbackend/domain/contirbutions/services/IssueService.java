package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.IssueFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueRepository;
import com.stergion.githubbackend.domain.contirbutions.search.IssueSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.IssueMapper;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class IssueService extends ContributionService<Issue, IssueSearchCriteria> {
    @Inject
    IssueMapper issueMapper;
    @Inject
    IssueSearchStrategy searchStrategy;

    @Inject
    public IssueService(IssueRepository issueRepository, IssueFetchStrategy fetchStrategy) {
        super(issueRepository, fetchStrategy);
    }


    public Multi<List<Issue>> fetchAndCreateIssues(String login, LocalDateTime from,
                                                   LocalDateTime to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }

//    public Uni<PagedResponse<Issue>> search(IssueSearchCriteria criteria) {
//        return searchStrategy.search(criteria)
//                             .map(response -> PagedResponse.map(response, issueMapper::toDomain));
//    }
}
