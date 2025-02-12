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
        assertNull(review.getUser().id());
        assertNull(review.getRepository().id());
        assertEquals(TEST_LOGIN, review.getUser().login());
        assertEquals("test-repo", review.getRepository().name());
        assertEquals("owner", review.getRepository().owner());
        assertEquals("123", review.getGithub().id());
        assertEquals(URI.create("https://example.com"), review.getGithub().url());
        assertEquals("pr123", review.getPullRequest().id());
        assertEquals(URI.create("https://example.com/pr"), review.getPullRequest().url());
        assertEquals(date, review.getCreatedAt());
        assertEquals(date, review.getUpdatedAt());
        assertEquals(date, review.getPublishedAt());
        assertEquals(date, review.getSubmittedAt());
        assertEquals(date, review.getLastEditedAt());
        assertEquals(PullRequestReviewState.APPROVED, review.getState());
        assertEquals("Review body", review.getBody());

        // Verify comments
        assertNotNull(review.getComments());
        assertEquals(1, review.getComments().size());

        PullRequestReviewComment comment = review.getComments().getFirst();
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
        assertNotNull(review.getComments());
        assertTrue(review.getComments().isEmpty());
        assertNull(review.getPublishedAt());
        assertNull(review.getSubmittedAt());
        assertNull(review.getLastEditedAt());
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