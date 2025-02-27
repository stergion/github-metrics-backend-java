package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueCommentEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

@ApplicationScoped
public final class IssueCommentRepositoryPostgres
        implements ContributionRepositoryPostgres<IssueCommentEntity> {
    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Long> deleteByUserId(UUID userId) {
        String deleteIssueCommentsQuery = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM IssueComments i
                    JOIN Contributions c ON i.id = c.id
                    WHERE c.userId = ?1
                ),
                delete_issue_comments AS (
                    DELETE FROM IssueComments
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
                        session.createNativeQuery(deleteIssueCommentsQuery)
                               .setParameter(1, userId)
                               .executeUpdate()
                               .map(Long::valueOf)
                                             );
    }

    @Override
    public Uni<Long> deleteAll() {
        String deleteIssueCommentsQuery = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM IssueComments i
                    JOIN Contributions c ON i.id = c.id
                ),
                delete_issue_comments AS (
                    DELETE FROM IssueComments
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
                        session.createNativeQuery(deleteIssueCommentsQuery)
                               .executeUpdate()
                               .map(Long::valueOf)
                                             );

    }
}
