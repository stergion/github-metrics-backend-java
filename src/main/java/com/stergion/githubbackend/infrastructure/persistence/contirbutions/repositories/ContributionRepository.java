package com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Contribution;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

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

}
