package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.PullRequestReviewSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestReviewField;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestReviewEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.PullRequestReviewRepository;
import io.quarkus.logging.Log;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoPullRequestReviewSearchStrategy
        extends MongoContributionSearchStrategy<PullRequestReviewEntity, PullRequestReviewSearchCriteria>
        implements PullRequestReviewSearchStrategy {

    public MongoPullRequestReviewSearchStrategy(PullRequestReviewRepository repository) {
        super(repository);
    }

    @Override
    public Bson createQuery(PullRequestReviewSearchCriteria criteria) {
        List<Bson> conditions = new ArrayList<>(createBaseConditions(criteria));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        criteria.getState().ifPresent(state ->
                conditions.add(Filters.eq(PullRequestReviewField.STATE.fieldName(), state)));

        var query = Filters.and(conditions);
        Log.debug("Mongo query: " + query.toBsonDocument().toJson());
        return query;
    }
}