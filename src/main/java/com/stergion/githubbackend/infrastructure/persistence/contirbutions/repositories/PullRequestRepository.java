package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.IssueState;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.PullRequestState;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public final class PullRequestRepository implements ContributionRepository<PullRequest> {
    public List<PullRequest> findByUserIdAndState(ObjectId testUserId, PullRequestState prState) {
        return list("userId =?1 and state =?2", testUserId, prState);
    }
}
