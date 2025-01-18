package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.IssueState;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public final class IssueRepository implements ContributionRepository<Issue> {
    public List<Issue> findByUserIdAndState(ObjectId testUserId, IssueState issueState) {
        return list("userId =?1 and state =?2", testUserId, issueState);
    }
}
