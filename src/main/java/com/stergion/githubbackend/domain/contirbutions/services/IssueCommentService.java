package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.IssueCommentDTO;
import com.stergion.githubbackend.domain.contirbutions.mappers.IssueCommentMapper;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.IssueComment;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.IssueCommentRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class IssueCommentService extends ContributionService<IssueCommentDTO, IssueComment> {
    @Inject
    IssueCommentMapper issueCommentMapper;

    @Inject
    public IssueCommentService(IssueCommentRepository issueCommentRepository) {
        super(issueCommentRepository);
    }


    @Override
    protected IssueComment mapDtoToEntity(IssueCommentDTO dto, ObjectId userId, ObjectId repoId) {
        return issueCommentMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected IssueCommentDTO mapEntityToDto(IssueComment entity) {
        return issueCommentMapper.toDTO(entity);
    }

    private Multi<IssueCommentDTO> fetchIssueComments(String login, LocalDate from, LocalDate to) {
        return client.getIssueComments(login, from, to);
    }

    private Multi<List<IssueCommentDTO>> fetchIssueComments(String login, LocalDate from, LocalDate to,
                                                            BatchProcessorConfig config) {
        return client.getIssueCommentsBatched(login, from, to, config);
    }

    public void fetchAndCreateIssueComments(String login, LocalDate from, LocalDate to) {
        var config = BatchProcessorConfig.defaultConfig();
        var issueComments = fetchIssueComments(login, from, to, config);

        createContributions(issueComments);
    }
}
