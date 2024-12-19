package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.IssueComment;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public class IssueCommentRepository implements PanacheMongoRepository<IssueComment> {
    //    TODO
    public List<IssueComment> findByUserId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    //    TODO
    public List<IssueComment> findByRepositoryId(ObjectId userId) {
        throw new NotImplementedYet();
    }

    //    TODO
    public IssueComment findByUserAndRepositoryId(ObjectId userId, ObjectId repositoryId) {
        throw new NotImplementedYet();
    }


}
