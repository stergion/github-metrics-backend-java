package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.PullRequestReview;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.jboss.resteasy.reactive.common.NotImplementedYet;

import java.util.List;

@ApplicationScoped
public final class PullRequestReviewRepository implements ContributionRepository<PullRequestReview> {
}
