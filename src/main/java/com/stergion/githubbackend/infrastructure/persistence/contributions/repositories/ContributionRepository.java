package com.stergion.githubbackend.infrastructure.persistence.contributions.repositories;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.Contribution;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

public sealed interface ContributionRepository<T extends Contribution>
        extends ReactivePanacheMongoRepository<T>
        permits CommitRepository, IssueRepository, IssueCommentRepository, PullRequestRepository,
        PullRequestReviewRepository {

    default Uni<T> findById(ObjectId id) {
        return find("_id", id).firstResult();
    }

    default Uni<T> findByGitHubId(String gitHubId) {
        return find("github.id = ?1", gitHubId).firstResult();
    }

    default Multi<T> findByUserId(ObjectId userId) {
        return find("userId = ?1", userId).stream();
    }

    default Multi<T> findByRepoId(ObjectId repoId) {
        return find("repositoryId = ?1", repoId).stream();
    }

    default Multi<T> findByUserAndRepoId(ObjectId userId, ObjectId repoId) {
        return find("userId = ?1 and repositoryId = ?2", userId, repoId).stream();
    }


    default Uni<List<ObjectId>> getRepositoryIds(ObjectId userId) {
        return mongoCollection().aggregate(Arrays.asList(
                                                Aggregates.match(Filters.eq("user_id", userId)),
                                                Aggregates.group("$repository_id")
                                                        ),
                                        Document.class)
                                .map(document -> document.get("_id", ObjectId.class))
                                .select().distinct()
                                .collect().asList();

    }

    default Uni<Void> delete(ObjectId userId) {
        return delete("userId = ?1", userId).replaceWithVoid();
    }
}
