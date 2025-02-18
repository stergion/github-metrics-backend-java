package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public final class IssueRepositoryPostgres implements ContributionRepositoryPostgres<IssueEntity> {
    @Inject
    Mutiny.SessionFactory sessionFactory;

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
