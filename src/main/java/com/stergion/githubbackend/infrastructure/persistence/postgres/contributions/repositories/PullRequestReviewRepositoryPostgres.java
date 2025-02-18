package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestReviewEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public final class PullRequestReviewRepositoryPostgres
        implements ContributionRepositoryPostgres<PullRequestReviewEntity> {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Long> deleteAll() {
        // Delete associations
        String deleteReviewsReviewCommentsQuery = "DELETE FROM PullRequestReviews_PullRequestReviewComments ";

        // Delete associated entities
        String deleteReviewCommentsQuery = "DELETE FROM PullRequestReviewComments";

        // Delete Reviews
        String deleteReviews = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM PullRequestReviews i
                    JOIN Contributions c ON i.id = c.id
                ),
                delete_reviews AS (
                    DELETE FROM PullRequestReviews
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
                        session.createNativeQuery(deleteReviewsReviewCommentsQuery)
                               .executeUpdate()
                               .flatMap(result ->
                                               session.createNativeQuery(deleteReviewCommentsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteReviews)
                                                      .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );

    }
}
