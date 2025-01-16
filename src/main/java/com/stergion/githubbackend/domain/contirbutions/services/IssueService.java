package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.dto.IssueDTO;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.IssueFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.IssueMapper;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.IssueRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class IssueService extends ContributionService<IssueDTO, Issue> {
    @Inject
    IssueMapper issueMapper;

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

    public Multi<List<IssueDTO>> fetchAndCreateIssues(String login, LocalDate from, LocalDate to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }
}
