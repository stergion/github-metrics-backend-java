package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestReviewEntity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

@ApplicationScoped
public final class PullRequestReviewRepositoryPostgres
        implements ContributionRepositoryPostgres<PullRequestReviewEntity> {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Long> deleteByUserId(UUID userId) {
        // Delete associations
        String deleteReviewsReviewCommentsQuery = """
                DELETE FROM PullRequestReviews_PullRequestReviewComments
                WHERE pullrequestreview_id IN (
                    SELECT prr.id
                    FROM PullRequestReviews prr
                    JOIN Contributions cont ON prr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        // Delete associated entities
        String deleteReviewCommentsQuery = """
                DELETE FROM PullRequestReviewComments
                WHERE id IN (
                    SELECT prc.id
                    FROM PullRequestReviewComments prc
                    JOIN PullRequestReviews_PullRequestReviewComments prrc ON prc.id = prrc.comments_id
                    JOIN PullRequestReviews prr ON prrc.pullrequestreview_id = prr.id
                    JOIN Contributions cont ON prr.id = cont.id
                    WHERE cont.userId = ?1
                )
                """;

        // Delete Reviews
        String deleteReviews = """
                WITH ids AS MATERIALIZED (
                    SELECT i.id
                    FROM PullRequestReviews i
                    JOIN Contributions c ON i.id = c.id
                    WHERE c.userId = ?1
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
                SELECT count(*) FROM ids
                """;

        return sessionFactory.withTransaction((session, tx) ->
                        session.createNativeQuery(deleteReviewsReviewCommentsQuery)
                               .setParameter(1, userId)
                               .executeUpdate()
                               .flatMap(result -> session.createNativeQuery(deleteReviewCommentsQuery)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .flatMap(result -> session.createNativeQuery(deleteReviews)
                                                         .setParameter(1, userId)
                                                         .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );
    }

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
