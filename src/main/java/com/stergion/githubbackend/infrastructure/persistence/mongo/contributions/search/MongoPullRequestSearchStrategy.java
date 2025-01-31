package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.PullRequestSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestField;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.PullRequestRepository;
import io.quarkus.logging.Log;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoPullRequestSearchStrategy
        extends MongoContributionSearchStrategy<PullRequest, PullRequestSearchCriteria>
        implements PullRequestSearchStrategy {

    public MongoPullRequestSearchStrategy(PullRequestRepository repository) {
        super(repository);
    }

    @Override
    public Bson createQuery(PullRequestSearchCriteria criteria) {
        List<Bson> conditions = new ArrayList<>(createBaseConditions(criteria));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        criteria.getState().ifPresent(state ->
                conditions.add(Filters.eq(PullRequestField.STATE.fieldName(), state)));

        var query = Filters.and(conditions);
        Log.debug("Mongo query: " + query.toBsonDocument().toJson());
        return query;
    }
}