package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Contribution;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public sealed interface ContributionRepository<T extends Contribution>
        extends PanacheMongoRepository<T>
        permits CommitRepository, IssueRepository, IssueCommentRepository, PullRequestRepository,
        PullRequestReviewRepository {

    default T findById(ObjectId id) {
        return find("id", id).firstResult();
    }

    default T findByGitHubId(String gitHubId) {
        return find("github.id = ?1", gitHubId).firstResult();
    }

    default List<T> findByUserId(ObjectId userId) {
        return list("userId = ?1", userId);
    }

    default List<T> findByRepoId(ObjectId repoId) {
        return list("repositoryId = ?1", repoId);
    }

    default List<T> findByUserAndRepoId(ObjectId userId, ObjectId repoId) {
        return list("userId = ?1 and repositoryId = ?2", userId, repoId);
    }


    default List<ObjectId> getRepositoryIds(ObjectId userId) {
        return mongoCollection().aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("userId", userId)),
                Aggregates.group("$repositoryId"),
                Aggregates.project(Projections.fields(
                                Projections.include("repositoryId"),
                                Projections.excludeId()))))
                .map(i -> i.repositoryId)
                .into(new ArrayList<>());
    }

    default void delete(ObjectId userId) {
        delete("userId = ?1", userId);
    }
}
