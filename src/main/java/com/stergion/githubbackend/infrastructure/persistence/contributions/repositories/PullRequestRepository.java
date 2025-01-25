package com.stergion.githubbackend.infrastructure.persistence.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.PullRequestState;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public final class PullRequestRepository implements ContributionRepository<PullRequest> {
    public Multi<PullRequest> findByUserIdAndState(ObjectId testUserId, PullRequestState prState) {
        return find("userId =?1 and state =?2", testUserId, prState).stream();
    }
}
