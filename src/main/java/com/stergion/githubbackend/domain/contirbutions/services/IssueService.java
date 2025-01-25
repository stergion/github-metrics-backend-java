package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.dto.IssueDTO;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.IssueFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.IssueMapper;
import com.stergion.githubbackend.domain.contirbutions.search.IssueSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.contributions.repositories.IssueRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class IssueService extends ContributionService<IssueDTO, Issue> {
    @Inject
    IssueMapper issueMapper;
    @Inject
    IssueSearchStrategy searchStrategy;

    @Inject
    public IssueService(IssueRepository issueRepository, IssueFetchStrategy fetchStrategy) {
        super(issueRepository, fetchStrategy);
    }

    @Override
    protected Issue mapDtoToEntity(IssueDTO dto, ObjectId userId, ObjectId repoId) {
        return issueMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected IssueDTO mapEntityToDto(Issue entity) {
        return issueMapper.toDTO(entity);
    }

    public Multi<List<IssueDTO>> fetchAndCreateIssues(String login, LocalDateTime from,
                                                      LocalDateTime to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }

    public Uni<PagedResponse<IssueDTO>> search(IssueSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response, issueMapper::toDTO));
    }
}
