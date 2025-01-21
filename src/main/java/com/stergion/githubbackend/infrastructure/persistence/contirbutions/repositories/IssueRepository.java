package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.IssueState;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public final class IssueRepository implements ContributionRepository<Issue> {
    public Multi<Issue> findByUserIdAndState(ObjectId testUserId, IssueState issueState) {
        return find("userId =?1 and state =?2", testUserId, issueState).stream();
    }
}
