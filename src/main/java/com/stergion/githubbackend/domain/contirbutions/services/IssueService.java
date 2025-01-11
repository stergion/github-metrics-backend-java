package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.IssueDTO;
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
    public IssueService(IssueRepository issueRepository) {
        super(issueRepository);
    }


    @Override
    protected Issue mapDtoToEntity(IssueDTO dto, ObjectId userId, ObjectId repoId) {
        return issueMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected IssueDTO mapEntityToDto(Issue entity) {
        return issueMapper.toDTO(entity);
    }

    private Multi<IssueDTO> fetchIssues(String login, LocalDate from, LocalDate to) {
        return client.getIssues(login, from, to);
    }

    private Multi<List<IssueDTO>> fetchIssues(String login, LocalDate from, LocalDate to,
                                              BatchProcessorConfig config) {
        return client.getIssuesBatched(login, from, to, config);
    }

    public void fetchAndCreateIssues(String login, LocalDate from, LocalDate to) {
        var config = BatchProcessorConfig.defaultConfig();
        var issues = fetchIssues(login, from, to, config);
        createContributions(issues);
    }
}
