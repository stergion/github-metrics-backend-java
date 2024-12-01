package com.stergion.githubbackend.core.contirbutions.repositories;

import com.stergion.githubbackend.core.contirbutions.entities.PullRequest;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public class PullRequestRepository implements PanacheMongoRepository<PullRequest> {
    //    TODO
    public List<PullRequest> findByUserId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    public List<PullRequest> findByRepositoryId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    //    TODO
    public PullRequest findByUserAndRepositoryId(ObjectId userId, ObjectId repositoryId) {
        throw new NotImplementedYet();
    }


}
