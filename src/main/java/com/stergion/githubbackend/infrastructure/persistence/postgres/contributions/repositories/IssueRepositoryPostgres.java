package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

@ApplicationScoped
public final class IssueRepositoryPostgres implements ContributionRepositoryPostgres<IssueEntity> {
    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Long> deleteByUserId(UUID userId) {
        // Delete associations
        String deleteIssuesLabelsQuery = """
                DELETE FROM Issues_Labels
                WHERE issue_id IN (
                    SELECT i.id
                    FROM Issues i
                    JOIN Contributions cont ON i.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        // Delete associated entities
        String deleteLabelsQuery = """
                DELETE FROM Labels
                WHERE id IN (
                    SELECT l.id
                    FROM Labels l
                    JOIN Issues_Labels il ON l.id = il.labels_id
                    JOIN Issues i ON il.issue_id = i.id
                    JOIN Contributions cont ON i.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        // Delete Issues
        String deleteIssues = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM Issues i
                    JOIN Contributions c ON i.id = c.id
                    WHERE c.userId = ?1
                ),
                delete_issues AS (
                    DELETE FROM Issues
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
                        session.createNativeQuery(deleteIssuesLabelsQuery)
                               .setParameter(1, userId)
                               .executeUpdate()
                               .flatMap(result -> session.createNativeQuery(deleteLabelsQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteIssues)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );
    }

    @Override
    public Uni<Long> deleteAll() {
        // Delete associations
        String deleteIssuesLabelsQuery = "DELETE FROM Issues_Labels ";

        // Delete associated entities
        String deleteLabelsQuery = "DELETE FROM Labels";

        // Delete Issues
        String deleteIssues =  """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM Issues i
                    JOIN Contributions c ON i.id = c.id
                ),
                delete_issues AS (
                    DELETE FROM Issues
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
                        session.createNativeQuery(deleteIssuesLabelsQuery)
                               .executeUpdate()
                               .flatMap(result ->
                                               session.createNativeQuery(deleteLabelsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteIssues)
                                                      .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );

    }

}
