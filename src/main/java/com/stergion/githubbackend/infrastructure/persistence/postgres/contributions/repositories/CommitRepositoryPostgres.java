package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.CommitEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

@ApplicationScoped
public final class CommitRepositoryPostgres
        implements ContributionRepositoryPostgres<CommitEntity> {
    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Long> deleteByUserId(UUID userId) {
        // First delete the mappings (join tables)
        String deleteCommitsAssociatedPullRequestsQuery = """
                DELETE FROM Commits_AssociatedPullRequests
                WHERE commit_id IN (
                    SELECT c.id
                    FROM Commits c
                    JOIN Contributions cont ON c.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deleteCommitsCommitCommentsQuery = """
                DELETE FROM Commits_CommitComments
                WHERE commit_id IN (
                    SELECT c.id
                    FROM Commits c
                    JOIN Contributions cont ON c.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deleteCommitsFilesQuery = """
                DELETE FROM Commits_Files
                WHERE commit_id IN (
                    SELECT c.id
                    FROM Commits c
                    JOIN Contributions cont ON c.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        // Then delete the actual entities
        String deleteAssociatedPullRequestsQuery = """
                DELETE FROM AssociatedPullRequests
                WHERE id IN (
                    SELECT apr.id
                    FROM AssociatedPullRequests apr
                    JOIN Commits_AssociatedPullRequests cm ON apr.id = cm.associatedPullRequest_id
                    JOIN Commits c ON cm.commit_id = c.id
                    JOIN Contributions cont ON c.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deleteCommitCommentsQuery = """
                DELETE FROM CommitComments
                WHERE id IN (
                    SELECT cc.id
                    FROM CommitComments cc
                    JOIN Commits_CommitComments cm ON cc.id = cm.comments_id
                    JOIN Commits c ON cm.commit_id = c.id
                    JOIN Contributions cont ON c.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deleteFilesQuery = """
                DELETE FROM Files
                WHERE id IN (
                    SELECT f.id
                    FROM Files f
                    JOIN Commits_Files cm ON f.id = cm.files_id
                    JOIN Commits c ON cm.commit_id = c.id
                    JOIN Contributions cont ON c.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        String deleteCommits = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM Commits i
                    JOIN Contributions c ON i.id = c.id
                    WHERE c.userId = ?1
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
                SELECT count(*) FROM ids
                """;

        return sessionFactory.withTransaction((session, tx) ->
                        session.createNativeQuery(deleteCommitsAssociatedPullRequestsQuery)
                               .setParameter(1, userId)
                               .executeUpdate()
                               .flatMap(
                                       result -> session.createNativeQuery(deleteCommitsCommitCommentsQuery)
                                                        .setParameter(1, userId)
                                                        .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteCommitsFilesQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               // Now delete the mapped entities
                               .flatMap(result -> session.createNativeQuery(
                                                                 deleteAssociatedPullRequestsQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteCommitCommentsQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteFilesQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteCommits)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );
    }

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
