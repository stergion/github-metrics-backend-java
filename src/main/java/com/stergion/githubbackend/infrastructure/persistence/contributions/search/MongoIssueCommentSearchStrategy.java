package com.stergion.githubbackend.infrastructure.persistence.contributions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.IssueCommentSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueCommentField;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.IssueComment;
import com.stergion.githubbackend.infrastructure.persistence.contributions.repositories.IssueCommentRepository;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoIssueCommentSearchStrategy
        extends MongoContributionSearchStrategy<IssueComment, IssueCommentSearchCriteria>
        implements IssueCommentSearchStrategy {

    public MongoIssueCommentSearchStrategy(IssueCommentRepository repository) {
        super(repository);
    }

    @Override
    public Bson createQuery(IssueCommentSearchCriteria criteria) {
        List<Bson> conditions = new ArrayList<>();
        conditions.add(
                Filters.eq(CommonField.USER_LOGIN.fieldName(), criteria.getUserLogin()));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        criteria.getSince().ifPresent(since ->
                conditions.add(Filters.gte(IssueCommentField.PUBLISHED_AT.fieldName(), since)));

        criteria.getUntil().ifPresent(until ->
                conditions.add(Filters.lte(IssueCommentField.PUBLISHED_AT.fieldName(), until)));

        return Filters.and(conditions);
    }
}