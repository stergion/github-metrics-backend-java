package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueEntity;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueState;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public final class IssueRepositoryMongo implements ContributionRepositoryMongo<IssueEntity> {
    public Multi<IssueEntity> findByUserIdAndState(ObjectId testUserId, IssueState issueState) {
        return find("userId =?1 and state =?2", testUserId, issueState).stream();
    }
}
