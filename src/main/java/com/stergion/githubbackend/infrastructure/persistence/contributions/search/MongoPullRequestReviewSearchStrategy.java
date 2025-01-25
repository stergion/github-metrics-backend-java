package com.stergion.githubbackend.infrastructure.persistence.contributions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.PullRequestReviewSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.PullRequestReviewSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.PullRequestReviewField;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.contributions.repositories.PullRequestReviewRepository;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoPullRequestReviewSearchStrategy
        extends MongoContributionSearchStrategy<PullRequestReview, PullRequestReviewSearchCriteria>
        implements PullRequestReviewSearchStrategy {

    public MongoPullRequestReviewSearchStrategy(PullRequestReviewRepository repository) {
        super(repository);
    }

    @Override
    public Bson createQuery(PullRequestReviewSearchCriteria criteria) {
        List<Bson> conditions = new ArrayList<>();
        conditions.add(
                Filters.eq(CommonField.USER_LOGIN.fieldName(), criteria.getUserLogin()));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        criteria.getState().ifPresent(state ->
                conditions.add(Filters.eq(PullRequestReviewField.STATE.fieldName(), state)));

        criteria.getSince().ifPresent(since ->
                conditions.add(Filters.gte(CommonField.CREATED_AT.fieldName(), since)));

        criteria.getUntil().ifPresent(until ->
                conditions.add(Filters.lte(CommonField.CREATED_AT.fieldName(), until)));

        return Filters.and(conditions);
    }
}