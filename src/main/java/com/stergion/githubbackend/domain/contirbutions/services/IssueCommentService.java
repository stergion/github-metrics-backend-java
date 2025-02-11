package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.IssueCommentFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.IssueCommentMapper;
import com.stergion.githubbackend.domain.contirbutions.search.IssueCommentSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.MongoIssueCommentRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class IssueCommentService extends ContributionService<IssueComment, IssueCommentEntity> {
    @Inject
    IssueCommentMapper issueCommentMapper;
    @Inject
    IssueCommentSearchStrategy searchStrategy;

    @Inject
    public IssueCommentService(MongoIssueCommentRepository issueCommentRepository,
                               IssueCommentFetchStrategy fetchStrategy) {
        super(issueCommentRepository, fetchStrategy);
    }


    @Override
    protected IssueCommentEntity mapDomainToEntity(IssueComment issueComment, ObjectId userId, ObjectId repoId) {
        return issueCommentMapper.toEntity(issueComment, userId, repoId);
    }

    @Override
    protected IssueComment mapEntityToDomain(IssueCommentEntity entity) {
        return issueCommentMapper.toDomain(entity);
    }

    public Multi<List<IssueComment>> fetchAndCreateIssueComments(String login,
                                                                 LocalDateTime from,
                                                                 LocalDateTime to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }

    public Uni<PagedResponse<IssueComment>> search(IssueCommentSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response,
                                     issueCommentMapper::toDomain));
    }
}
