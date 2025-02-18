package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

@ApplicationScoped
public final class PullRequestRepositoryPostgres
        implements ContributionRepositoryPostgres<PullRequestEntity> {
    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Long> deleteByUserId(UUID userId) {
        // Delete associations
        String deletePullRequestsLabelsQuery = """
                DELETE FROM PullRequests_Labels
                WHERE pullrequest_id IN (
                    SELECT pr.id
                    FROM PullRequests pr
                    JOIN Contributions cont ON pr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deletePullRequestsPullRequestCommitsQuery = """
                DELETE FROM PullRequests_PullRequestCommits
                WHERE pullrequest_id IN (
                    SELECT pr.id
                    FROM PullRequests pr
                    JOIN Contributions cont ON pr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deletePullRequestsClosingIssuesReferencesQuery = """
                DELETE FROM PullRequests_ClosingIssuesReferences
                WHERE pullrequest_id IN (
                    SELECT pr.id
                    FROM PullRequests pr
                    JOIN Contributions cont ON pr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        // Delete associated entities
        String deleteLabelsQuery = """
                DELETE FROM Labels
                WHERE id IN (
                    SELECT l.id
                    FROM Labels l
                    JOIN PullRequests_Labels pl ON l.id = pl.labels_id
                    JOIN PullRequests pr ON pl.pullrequest_id = pr.id
                    JOIN Contributions cont ON pr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deletePullRequestCommitsQuery = """
                DELETE FROM PullRequestCommits
                WHERE id IN (
                    SELECT prc.id
                    FROM PullRequestCommits prc
                    JOIN PullRequests_PullRequestCommits pl ON prc.id = pl.commits_id
                    JOIN PullRequests pr ON pl.pullrequest_id = pr.id
                    JOIN Contributions cont ON pr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deleteClosingIssuesReferencesQuery = """
                DELETE FROM ClosingIssuesReferences
                WHERE id IN (
                    SELECT cir.id
                    FROM ClosingIssuesReferences cir
                    JOIN PullRequests_ClosingIssuesReferences pcir ON cir.id = pcir.closingIssuesReferences_id
                    JOIN PullRequests pr ON pcir.pullrequest_id = pr.id
                    JOIN Contributions cont ON pr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        // Delete PullRequests
        String deletePullRequests = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM PullRequests i
                    JOIN Contributions c ON i.id = c.id
                    WHERE c.userId = ?1
                ),
                delete_pull_requests AS (
                    DELETE FROM PullRequests
                    WHERE id IN (SELECT id FROM ids)
                ),
                delete_contributions AS (
                    DELETE FROM Contributions
                    WHERE id IN (SELECT id FROM ids)
                    RETURNING id
                )
                SELECT count(*) FROM ids
                """;

        return sessionFactory.withTransaction((session, tx) ->
                        session.createNativeQuery(deletePullRequestsLabelsQuery)
                               .setParameter(1, userId)
                               .executeUpdate()
                               .flatMap(result -> session.createNativeQuery(deletePullRequestsPullRequestCommitsQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deletePullRequestsClosingIssuesReferencesQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteLabelsQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deletePullRequestCommitsQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteClosingIssuesReferencesQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deletePullRequests)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );
    }

    @Override
    public Uni<Long> deleteAll() {
        // Delete associations
        String deletePullRequestsLabelsQuery = "DELETE FROM PullRequests_Labels";
        String deletePullRequestsPullRequestCommitsQuery = "DELETE FROM PullRequests_PullRequestCommits";
        String deletePullRequestsClosingIssuesReferencesQuery = "DELETE FROM PullRequests_ClosingIssuesReferences";

        // Delete associated entities
        String deleteLabelsQuery = "DELETE FROM Labels";
        String deletePullRequestCommitsQuery = "DELETE FROM PullRequestCommits";
        String deleteClosingIssuesReferencesQuery = "DELETE FROM ClosingIssuesReferences";

        // Delete PullRequests
        String deletePullRequests = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM PullRequests i
                    JOIN Contributions c ON i.id = c.id
                ),
                delete_pull_requests AS (
                    DELETE FROM PullRequests
                    WHERE id IN (SELECT id FROM ids)
                ),
                delete_contributions AS (
                DELETE FROM Contributions
                WHERE id IN (SELECT id FROM ids)
                RETURNING id
                )
                SELECT count(*) FROM ids id
                """;

        return sessionFactory.withTransaction((session, tx) ->
                        session.createNativeQuery(deletePullRequestsLabelsQuery)
                               .executeUpdate()
                               .flatMap(result ->
                                               session.createNativeQuery(
                                                              deletePullRequestsPullRequestCommitsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deletePullRequestsClosingIssuesReferencesQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteLabelsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(
                                                              deletePullRequestCommitsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteClosingIssuesReferencesQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deletePullRequests)
                                                      .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );

    }

}
