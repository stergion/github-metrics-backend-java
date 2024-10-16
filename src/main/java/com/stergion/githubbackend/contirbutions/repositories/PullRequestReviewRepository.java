package com.stergion.githubbackend.contirbutions.repositories;

import com.stergion.githubbackend.contirbutions.entities.PullRequestReview;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public class PullRequestReviewRepository implements PanacheMongoRepository<PullRequestReview> {
    //    TODO
    public List<PullRequestReview> findByUserId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    //    TODO
    public List<PullRequestReview> findByRepositoryId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    //    TODO
    public PullRequestReview findByUserAndRepositoryId(ObjectId userId, ObjectId repositoryId) {
        throw new NotImplementedYet();
    }


}
