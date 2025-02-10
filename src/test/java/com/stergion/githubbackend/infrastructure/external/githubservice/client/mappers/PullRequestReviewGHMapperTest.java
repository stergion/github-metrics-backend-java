package com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers;

import com.stergion.githubbackend.domain.contirbutions.models.PullRequestReview;
import com.stergion.githubbackend.domain.utils.types.PullRequestReviewComment;
import com.stergion.githubbackend.domain.utils.types.PullRequestReviewState;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.PullRequestReviewGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PullRequestReviewGHMapperTest {

    @Inject
    PullRequestReviewGHMapper mapper;

    private static final String TEST_LOGIN = "testUser";

    @Test
    @DisplayName("Should map all fields correctly")
    void shouldMapAllFieldsCorrectly() {
        // Given
        LocalDate date = LocalDate.now();
        var reviewGH = createFullPullRequestReview(date);

        // When
        PullRequestReview review = mapper.toDomain(reviewGH, TEST_LOGIN);

        // Then
        assertNotNull(review);
        assertEquals(TEST_LOGIN, review.user());
        assertEquals("test-repo", review.repository().name());
        assertEquals("owner", review.repository().owner());
        assertEquals("123", review.github().id());
        assertEquals(URI.create("https://example.com"), review.github().url());
        assertEquals("pr123", review.pullRequest().id());
        assertEquals(URI.create("https://example.com/pr"), review.pullRequest().url());
        assertEquals(date, review.createdAt());
        assertEquals(date, review.updatedAt());
        assertEquals(date, review.publishedAt());
        assertEquals(date, review.submittedAt());
        assertEquals(date, review.lastEditedAt());
        assertEquals(PullRequestReviewState.APPROVED, review.state());
        assertEquals("Review body", review.body());

        // Verify comments
        assertNotNull(review.comments());
        assertEquals(1, review.comments().size());

        PullRequestReviewComment comment = review.comments().getFirst();
        assertEquals("comment-author", comment.login());
        assertEquals("comment1", comment.github().id());
        assertEquals("Comment body", comment.body());
        assertEquals(URI.create("https://example.com/comment"), comment.github().url());
    }

    @Test
    @DisplayName("Should handle null collections and optional fields properly")
    void shouldHandleNullCollections() {
        // Given
        LocalDate date = LocalDate.now();
        var reviewGH = createMinimalPullRequestReview(date);

        // When
        PullRequestReview review = mapper.toDomain(reviewGH, TEST_LOGIN);

        // Then
        assertNotNull(review);
        assertNotNull(review.comments());
        assertTrue(review.comments().isEmpty());
        assertNull(review.publishedAt());
        assertNull(review.submittedAt());
        assertNull(review.lastEditedAt());
    }

    private PullRequestReviewGH createFullPullRequestReview(LocalDate date) {
        return new PullRequestReviewGH(
                "123",
                URI.create("https://example.com"),
                date,
                date,
                date,
                date,
                date,
                PullRequestReviewGH.ReviewState.APPROVED,
                "Review body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                new GitHubNodeRef("pr123", URI.create("https://example.com/pr")),
                createCommentsConnection()
        );
    }

    private PullRequestReviewGH createMinimalPullRequestReview(LocalDate date) {
        return new PullRequestReviewGH(
                "123",
                URI.create("https://example.com"),
                date,
                date,
                null,
                null,
                null,
                PullRequestReviewGH.ReviewState.PENDING,
                "Review body",
                new RepositoryRef("test-repo", new RepositoryOwner("owner")),
                new GitHubNodeRef("pr123", URI.create("https://example.com/pr")),
                new PullRequestReviewGH.CommentsConnection(0, null)
        );
    }

    private PullRequestReviewGH.CommentsConnection createCommentsConnection() {
        var comment = new PullRequestReviewGH.ReviewComment(
                "comment1",
                URI.create("https://example.com/comment"),
                "Comment body",
                new Author("comment-author")
        );
        return new PullRequestReviewGH.CommentsConnection(1, List.of(comment));
    }
}