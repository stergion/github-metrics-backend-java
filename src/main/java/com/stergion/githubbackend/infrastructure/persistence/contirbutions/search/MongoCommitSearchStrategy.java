package com.stergion.githubbackend.infrastructure.persistence.contirbutions.search;

import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.CommitSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Commit;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.CommitRepository;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoCommitSearchStrategy
        extends MongoContributionSearchStrategy<Commit, CommitSearchCriteria>
        implements CommitSearchStrategy {

    public MongoCommitSearchStrategy(CommitRepository repository) {
        super(repository);
    }

    @Override
    public Bson createQuery(CommitSearchCriteria criteria) {
        List<Bson> conditions = new ArrayList<>();
        conditions.add(
                Filters.eq(CommonField.USER_LOGIN.fieldName(), criteria.getUserLogin()));

        criteria.getOwner().ifPresent(owner ->
                conditions.add(Filters.eq(CommonField.OWNER.fieldName(), owner)));

        criteria.getName().ifPresent(name ->
                conditions.add(Filters.eq(CommonField.NAME.fieldName(), name)));

        criteria.getSince().ifPresent(since ->
                conditions.add(Filters.gte(CommitField.COMMITED_DATE.fieldName(), since)));

        criteria.getUntil().ifPresent(until ->
                conditions.add(Filters.lte(CommitField.COMMITED_DATE.fieldName(), until)));

        return Filters.and(conditions);
    }
}