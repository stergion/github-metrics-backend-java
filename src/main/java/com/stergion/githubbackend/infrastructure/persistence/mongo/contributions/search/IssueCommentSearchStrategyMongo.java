package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.IssueCommentSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.IssueCommentSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.IssueCommentRepositoryMongo;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IssueCommentSearchStrategyMongo
        extends ContributionSearchStrategyMongo<IssueCommentEntity, IssueCommentSearchCriteria>
        implements IssueCommentSearchStrategy {

    public IssueCommentSearchStrategyMongo(IssueCommentRepositoryMongo repository) {
        super(repository);
    }

    @Override
    public Bson createQuery(IssueCommentSearchCriteria criteria) {
        List<Bson> conditions = new ArrayList<>(createBaseConditions(criteria));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        var query = Filters.and(conditions);
        Log.debug("Mongo query: " + query.toBsonDocument().toJson());
        return query;
    }
}