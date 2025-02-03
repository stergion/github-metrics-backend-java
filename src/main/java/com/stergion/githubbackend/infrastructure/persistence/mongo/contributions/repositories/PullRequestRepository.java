package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestState;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public final class PullRequestRepository implements ContributionRepository<PullRequest> {
    public Multi<PullRequest> findByUserIdAndState(ObjectId testUserId, PullRequestState prState) {
        return find("userId =?1 and state =?2", testUserId, prState).stream();
    }
}
