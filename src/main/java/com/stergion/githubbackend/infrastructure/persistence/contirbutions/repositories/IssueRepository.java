package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Issue;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public class IssueRepository implements PanacheMongoRepository<Issue> {
    //    TODO
    public List<Issue> findByUserId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    //    TODO
    public List<Issue> findByRepositoryId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    //    TODO
    public Issue findByUserAndRepositoryId(ObjectId userId, ObjectId repositoryId) {
        throw new NotImplementedYet();
    }


}
