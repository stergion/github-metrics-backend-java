package com.stergion.githubbackend.contirbutions.repositories;

import com.stergion.githubbackend.contirbutions.entities.Contribution;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public class ContributionRepository implements PanacheMongoRepository<Contribution> {
    public Contribution findByUserAndRepo(ObjectId userId, ObjectId repoId) {
        System.out.println("userId = " + userId + ", repoId = " + repoId);
        return find("userId = ?1 and userId = ?2", userId, repoId).firstResult();
    }
}
