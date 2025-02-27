package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.IssueCommentFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.models.IssueComment;
import com.stergion.githubbackend.domain.contirbutions.repositories.IssueCommentRepository;
import com.stergion.githubbackend.domain.contirbutions.search.IssueCommentSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.mappers.IssueCommentMapper;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class IssueCommentService
        extends ContributionService<IssueComment, IssueCommentSearchCriteria> {
    @Inject
    IssueCommentMapper issueCommentMapper;
    @Inject
    IssueCommentSearchStrategy searchStrategy;

    @Inject
    public IssueCommentService(IssueCommentRepository issueCommentRepository,
                               IssueCommentFetchStrategy fetchStrategy) {
        super(issueCommentRepository, fetchStrategy);
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

//    public Uni<PagedResponse<IssueComment>> search(IssueCommentSearchCriteria criteria) {
//        return searchStrategy.search(criteria)
//                             .map(response -> PagedResponse.map(response,
//                                     issueCommentMapper::toDomain));
//    }
}
