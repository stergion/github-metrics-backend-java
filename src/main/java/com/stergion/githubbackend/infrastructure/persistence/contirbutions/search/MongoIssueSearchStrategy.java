package com.stergion.githubbackend.infrastructure.persistence.contirbutions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.common.Database;
import com.stergion.githubbackend.common.DatabaseType;
import com.stergion.githubbackend.domain.contirbutions.search.IssueSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueField;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.IssueRepository;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoIssueSearchStrategy
        extends MongoContributionSearchStrategy<Issue, IssueSearchCriteria>
        implements IssueSearchStrategy {

    public MongoIssueSearchStrategy(IssueRepository repository) {
        super(repository);
    }

    @Override
    public Bson createQuery(IssueSearchCriteria criteria) {
        List<Bson> conditions = new ArrayList<>();
        conditions.add(
                Filters.eq(CommonField.USER_LOGIN.fieldName(), criteria.getUserLogin()));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        criteria.getState().ifPresent(state ->
                conditions.add(Filters.eq(IssueField.STATE.fieldName(), state)));

        criteria.getSince().ifPresent(since ->
                conditions.add(Filters.gte(CommonField.CREATED_AT.fieldName(), since)));

        criteria.getUntil().ifPresent(until ->
                conditions.add(Filters.lte(CommonField.CREATED_AT.fieldName(), until)));

        return Filters.and(conditions);
    }
}