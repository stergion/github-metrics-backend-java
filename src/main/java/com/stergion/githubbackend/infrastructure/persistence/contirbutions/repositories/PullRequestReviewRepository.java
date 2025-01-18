package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.PullRequestReviewState;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public final class PullRequestReviewRepository
        implements ContributionRepository<PullRequestReview> {
    public List<PullRequestReview> findByUserIdAndState(ObjectId testUserId,
                                                        PullRequestReviewState prReviewState) {
        return list("userId =?1 and state =?2", testUserId, prReviewState);
    }
}
