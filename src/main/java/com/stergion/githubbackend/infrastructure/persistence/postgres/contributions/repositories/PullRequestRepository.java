package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public final class PullRequestRepository implements ContributionRepository<PullRequestEntity> {
    @Inject
    Mutiny.SessionFactory sessionFactory;

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
