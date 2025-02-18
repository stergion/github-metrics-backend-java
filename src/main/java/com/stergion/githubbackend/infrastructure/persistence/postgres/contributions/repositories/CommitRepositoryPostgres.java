package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.CommitEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public final class CommitRepositoryPostgres
        implements ContributionRepositoryPostgres<CommitEntity> {
    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Long> deleteAll() {
        // Delete associations
        String deleteCommitsAssociatedPullRequestsQuery = "DELETE FROM " +
                                                          "Commits_AssociatedPullRequests";
        String deleteCommitsCommitCommentsQuery = "DELETE FROM Commits_CommitComments";
        String deleteCommitsFilesQuery = "DELETE FROM Commits_Files";

        // Delete associated entities
        String deleteAssociatedPullRequestsQuery = "DELETE FROM AssociatedPullRequests";
        String deleteCommitCommentsQuery = "DELETE FROM CommitComments";
        String deleteFilesQuery = "DELETE FROM Files";

        // Delete commits
        String deleteCommits = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM Commits i
                    JOIN Contributions c ON i.id = c.id
                ),
                delete_commits AS (
                    DELETE FROM Commits
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
                        session.createNativeQuery(deleteCommitsAssociatedPullRequestsQuery)
                               .executeUpdate()
                               .flatMap(result ->
                                               session.createNativeQuery(deleteCommitsCommitCommentsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteCommitsFilesQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteAssociatedPullRequestsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteCommitCommentsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteFilesQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteCommits)
                                                      .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );

    }
}
