package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.dto.IssueCommentDTO;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.IssueCommentFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.IssueCommentMapper;
import com.stergion.githubbackend.domain.contirbutions.search.IssueCommentSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.IssueComment;
import com.stergion.githubbackend.infrastructure.persistence.contributions.repositories.IssueCommentRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class IssueCommentService extends ContributionService<IssueCommentDTO, IssueComment> {
    @Inject
    IssueCommentMapper issueCommentMapper;
    @Inject
    IssueCommentSearchStrategy searchStrategy;

    @Inject
    public IssueCommentService(IssueCommentRepository issueCommentRepository,
                               IssueCommentFetchStrategy fetchStrategy) {
        super(issueCommentRepository, fetchStrategy);
    }


    @Override
    protected IssueComment mapDtoToEntity(IssueCommentDTO dto, ObjectId userId, ObjectId repoId) {
        return issueCommentMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected IssueCommentDTO mapEntityToDto(IssueComment entity) {
        return issueCommentMapper.toDTO(entity);
    }

    public Multi<List<IssueCommentDTO>> fetchAndCreateIssueComments(String login,
                                                                    LocalDateTime from,
                                                                    LocalDateTime to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }

    public Uni<PagedResponse<IssueCommentDTO>> search(IssueCommentSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     issueCommentMapper::toDTO));
    }
}
