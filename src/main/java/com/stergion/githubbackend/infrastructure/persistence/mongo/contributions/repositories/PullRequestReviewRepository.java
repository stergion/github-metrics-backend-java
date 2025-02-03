package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestReviewState;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public final class PullRequestReviewRepository
        implements ContributionRepository<PullRequestReview> {
    public Multi<PullRequestReview> findByUserIdAndState(ObjectId testUserId,
                                                         PullRequestReviewState prReviewState) {
        return find("userId =?1 and state =?2", testUserId, prReviewState).stream();
    }
}
