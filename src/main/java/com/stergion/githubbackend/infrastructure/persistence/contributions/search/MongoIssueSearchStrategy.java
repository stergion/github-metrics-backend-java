package com.stergion.githubbackend.infrastructure.persistence.contributions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.IssueSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.IssueField;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.contributions.repositories.IssueRepository;
import io.quarkus.logging.Log;
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
        List<Bson> conditions = new ArrayList<>(createBaseConditions(criteria));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        criteria.getState().ifPresent(state ->
                conditions.add(Filters.eq(IssueField.STATE.fieldName(), state)));

        var query = Filters.and(conditions);
        Log.debug("Mongo query: " + query.toBsonDocument().toJson());
        return query;
    }
}