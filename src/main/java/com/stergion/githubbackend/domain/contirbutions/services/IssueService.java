package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.models.Issue;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.IssueFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.IssueMapper;
import com.stergion.githubbackend.domain.contirbutions.search.IssueSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.MongoIssueRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class IssueService extends ContributionService<Issue, IssueEntity> {
    @Inject
    IssueMapper issueMapper;
    @Inject
    IssueSearchStrategy searchStrategy;

    @Inject
    public IssueService(MongoIssueRepository issueRepository, IssueFetchStrategy fetchStrategy) {
        super(issueRepository, fetchStrategy);
    }

    @Override
    protected IssueEntity mapDomainToEntity(Issue issue, ObjectId userId, ObjectId repoId) {
        return issueMapper.toEntity(issue, userId, repoId);
    }

    @Override
    protected Issue mapEntityToDomain(IssueEntity entity) {
        return issueMapper.toDomain(entity);
    }

    public Multi<List<Issue>> fetchAndCreateIssues(String login, LocalDateTime from,
                                                   LocalDateTime to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }

    public Uni<PagedResponse<Issue>> search(IssueSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response, issueMapper::toDomain));
    }
}
