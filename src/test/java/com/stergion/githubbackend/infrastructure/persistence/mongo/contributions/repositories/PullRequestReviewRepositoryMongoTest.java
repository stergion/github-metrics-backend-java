package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequestReviewEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types.Github;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types.PullRequestReviewComment;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types.UserWithLogin;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestReviewState;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("PullRequestReview Repository Tests")
class PullRequestReviewRepositoryMongoTest {

    @Inject
    PullRequestReviewRepositoryMongo pullRequestReviewRepository;

    private static final ObjectId TEST_USER_ID = new ObjectId();
    private static final ObjectId TEST_REPO_ID = new ObjectId();
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private PullRequestReviewEntity testReview;

    @BeforeEach
    void setUp() {
        pullRequestReviewRepository.deleteAll().await().indefinitely();
        testReview = createTestPullRequestReview(null, TEST_USER_ID, TEST_REPO_ID, Optional.empty());
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("Should persist and retrieve review by ID")
        void persistAndFindById() {
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            assertNotNull(testReview.id(), "Review ID should be generated");

            PullRequestReviewEntity found = pullRequestReviewRepository.findById(testReview.id())
                                                                       .await()
                                                                       .atMost(TIMEOUT);
            assertNotNull(found, "Found review should not be null");
            assertEquals(testReview.id(), found.id());
            assertEquals(testReview.userId(), found.userId());
            assertEquals(testReview.repositoryId(), found.repositoryId());
            assertEquals(testReview.github().id, found.github().id);
            assertEquals(testReview.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should delete review by user ID")
        void deleteByUserId() {
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            pullRequestReviewRepository.deleteByUserId(TEST_USER_ID).await().atMost(TIMEOUT);

            assertNull(
                    pullRequestReviewRepository.findById(testReview.id()).await().atMost(TIMEOUT),
                    "Review should be deleted");
        }
    }

    @Nested
    @DisplayName("Search Operations")
    class SearchOperations {

        @Test
        @DisplayName("Should find review by GitHub ID")
        void findByGitHubId() {
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            PullRequestReviewEntity found = pullRequestReviewRepository.findByGitHubId(
                    testReview.github().id).await().atMost(TIMEOUT);
            assertNotNull(found, "Found review should not be null");
            assertEquals(testReview.id(), found.id());
            assertEquals(testReview.userId(), found.userId());
            assertEquals(testReview.repositoryId(), found.repositoryId());
            assertEquals(testReview.github().id, found.github().id);
            assertEquals(testReview.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should find reviews by user ID")
        void findByUserId() {
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            List<PullRequestReviewEntity> found = pullRequestReviewRepository.findByUserId(TEST_USER_ID)
                                                                             .collect().asList()
                                                                             .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one review");
            assertNotNull(found, "Found reviews should not be null");
            assertEquals(testReview.id(), found.getFirst().id());
            assertEquals(testReview.userId(), found.getFirst().userId());
            assertEquals(testReview.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testReview.github().id, found.getFirst().github().id);
            assertEquals(testReview.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find reviews by repository ID")
        void findByRepoId() {
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            List<PullRequestReviewEntity> found = pullRequestReviewRepository.findByRepoId(TEST_REPO_ID)
                                                                             .collect().asList()
                                                                             .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one review");
            assertNotNull(found, "Found reviews should not be null");
            assertEquals(testReview.id(), found.getFirst().id());
            assertEquals(testReview.userId(), found.getFirst().userId());
            assertEquals(testReview.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testReview.github().id, found.getFirst().github().id);
            assertEquals(testReview.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find reviews by user ID and repository ID")
        void findByUserAndRepoId() {
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            List<PullRequestReviewEntity> found = pullRequestReviewRepository.findByUserAndRepoId(
                                                                               TEST_USER_ID,
                                                                               TEST_REPO_ID)
                                                                             .collect().asList()
                                                                             .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one review");
            assertNotNull(found, "Found reviews should not be null");
            assertEquals(testReview.id(), found.getFirst().id());
            assertEquals(testReview.userId(), found.getFirst().userId());
            assertEquals(testReview.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testReview.github().id, found.getFirst().github().id);
            assertEquals(testReview.createdAt(), found.getFirst().createdAt());
        }
    }

    @Nested
    @DisplayName("Repository Operations")
    class RepositoryOperations {

        @Test
        @DisplayName("Should get distinct repository IDs for user")
        void getRepositoryIds() {
            ObjectId userId1 = new ObjectId();
            ObjectId userId2 = new ObjectId();
            ObjectId repoId1 = new ObjectId();
            ObjectId repoId2 = new ObjectId();

            // Create multiple reviews for testing
            persistTestReviews(userId1, userId2, repoId1, repoId2);

            var repositoryIds = pullRequestReviewRepository.getRepositoryIds(userId1)
                                                           .await()
                                                           .atMost(TIMEOUT);

            assertEquals(2, repositoryIds.size(), "Should find two distinct repositories");
            assertTrue(repositoryIds.contains(repoId1), "Should contain first repo ID");
            assertTrue(repositoryIds.contains(repoId2), "Should contain second repo ID");
        }

        @Test
        @DisplayName("Should return empty list when user has no reviews")
        void getRepositoryIdsForUserWithNoReviews() {
            var repositoryIds = pullRequestReviewRepository.getRepositoryIds(new ObjectId())
                                                           .await()
                                                           .atMost(TIMEOUT);

            assertTrue(repositoryIds.isEmpty(),
                    "Should return empty list for user with no reviews");
        }
    }

    @Nested
    @DisplayName("Review State Operations")
    class ReviewStateOperations {
        @Test
        @DisplayName("Should find reviews by user ID and state")
        void findByUserIdAndState() {
            // Create reviews in different states
            ObjectId userId = new ObjectId();

            var pendingReview = createTestPullRequestReview(null, userId, TEST_REPO_ID, Optional.of("pending-1"));
            pendingReview.state = PullRequestReviewState.PENDING;

            var approvedReview = createTestPullRequestReview(null, userId, TEST_REPO_ID, Optional.of("approved-1"));
            approvedReview.state = PullRequestReviewState.APPROVED;
            approvedReview.submittedAt = LocalDate.now();

            var changesReview = createTestPullRequestReview(null, userId, TEST_REPO_ID, Optional.of("changes-1"));
            changesReview.state = PullRequestReviewState.CHANGES_REQUESTED;
            changesReview.submittedAt = LocalDate.now();

            pullRequestReviewRepository.persist(pendingReview).await().atMost(TIMEOUT);
            pullRequestReviewRepository.persist(approvedReview).await().atMost(TIMEOUT);
            pullRequestReviewRepository.persist(changesReview).await().atMost(TIMEOUT);

            // Test finding each state
            var pendingReviews = pullRequestReviewRepository.findByUserIdAndState(userId,
                                                                    PullRequestReviewState.PENDING)
                                                            .collect().asList()
                                                            .await().atMost(TIMEOUT);
            assertEquals(1, pendingReviews.size(), "Should find one pending review");
            assertEquals(PullRequestReviewState.PENDING, pendingReviews.getFirst().state(),
                    "Review should be pending");
            assertNull(pendingReviews.getFirst().submittedAt(),
                    "Pending review should not have submittedAt date");

            var approvedReviews = pullRequestReviewRepository.findByUserIdAndState(userId,
                                                                     PullRequestReviewState.APPROVED)
                                                             .collect().asList()
                                                             .await().atMost(TIMEOUT);
            assertEquals(1, approvedReviews.size(), "Should find one approved review");
            assertEquals(PullRequestReviewState.APPROVED, approvedReviews.getFirst().state(),
                    "Review should be approved");
            assertNotNull(approvedReviews.getFirst().submittedAt(),
                    "Approved review should have submittedAt date");

            var changesReviews = pullRequestReviewRepository.findByUserIdAndState(userId,
                                                                    PullRequestReviewState.CHANGES_REQUESTED)
                                                            .collect().asList()
                                                            .await().atMost(TIMEOUT);
            assertEquals(1, changesReviews.size(), "Should find one review requesting changes");
            assertEquals(PullRequestReviewState.CHANGES_REQUESTED,
                    changesReviews.getFirst().state(), "Review should request changes");
            assertNotNull(changesReviews.getFirst().submittedAt(),
                    "Review requesting changes should have submittedAt date");
        }

        @Test
        @DisplayName("Should handle different review states")
        void handleDifferentReviewStates() {
            testReview.state = PullRequestReviewState.APPROVED;
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            PullRequestReviewEntity found = pullRequestReviewRepository.findById(testReview.id())
                                                                       .await()
                                                                       .atMost(TIMEOUT);
            assertNotNull(found, "Found review should not be null");
            assertEquals(PullRequestReviewState.APPROVED, found.state(),
                    "Review state should match");
        }

        @Test
        @DisplayName("Should handle review with comments")
        void handleReviewWithComments() {
            PullRequestReviewComment comment = new PullRequestReviewComment();
            comment.body = "Test comment";
            testReview.comments = List.of(comment);

            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            PullRequestReviewEntity found = pullRequestReviewRepository.findById(testReview.id())
                                                                       .await()
                                                                       .atMost(TIMEOUT);
            assertNotNull(found.comments(), "Comments should not be null");
            assertFalse(found.comments().isEmpty(), "Comments should not be empty");
            assertEquals("Test comment", found.comments().getFirst().body,
                    "Comment body should match");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null values in optional fields")
        void handleNullOptionalFields() {
            PullRequestReviewEntity reviewWithNulls = createTestPullRequestReview(null, TEST_USER_ID,
                    TEST_REPO_ID, Optional.of("1"));
            reviewWithNulls.submittedAt = null;
            reviewWithNulls.updatedAt = null;
            reviewWithNulls.publishedAt = null;
            reviewWithNulls.lastEditedAt = null;
            reviewWithNulls.body = null;
            reviewWithNulls.comments = null;

            pullRequestReviewRepository.persist(reviewWithNulls).await().atMost(TIMEOUT);

            PullRequestReviewEntity found = pullRequestReviewRepository.findById(reviewWithNulls.id())
                                                                       .await()
                                                                       .atMost(TIMEOUT);

            assertNotNull(found, "Found review should not be null");
            assertEquals(reviewWithNulls.id(), found.id());
            assertEquals(reviewWithNulls.userId(), found.userId());
            assertEquals(reviewWithNulls.repositoryId(), found.repositoryId());
            assertEquals(reviewWithNulls.github().id, found.github().id);
            assertEquals(reviewWithNulls.createdAt(), found.createdAt());
            assertNull(found.submittedAt(), "SubmittedAt should be null");
            assertNull(found.updatedAt(), "UpdatedAt should be null");
            assertNull(found.publishedAt(), "PublishedAt should be null");
            assertNull(found.lastEditedAt(), "LastEditedAt should be null");
            assertNull(found.body(), "Body should be null");
            assertNull(found.comments(), "Comments should be null");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100})
        @DisplayName("Should handle different review counts")
        void handleDifferentReviewCounts(int reviewCount) {
            for (int i = 0; i < reviewCount; i++) {
                pullRequestReviewRepository.persist(
                                                   createTestPullRequestReview(null, TEST_USER_ID
                                                           , TEST_REPO_ID, Optional.of(
                                                                   String.valueOf(i))))
                                           .await()
                                           .atMost(TIMEOUT);
            }

            List<PullRequestReviewEntity> found = pullRequestReviewRepository.findByUserId(TEST_USER_ID)
                                                                             .collect().asList()
                                                                             .await().atMost(TIMEOUT);
            assertEquals(reviewCount, found.size(),
                    "Should find correct number of reviews");
        }
    }

    @Nested
    @DisplayName("Review State Transition Operations")
    class ReviewStateTransitionOperations {
        @Test
        @DisplayName("Should handle complete review lifecycle")
        void handleReviewLifecycle() {
            // Create pending review
            testReview.state = PullRequestReviewState.PENDING;
            testReview.submittedAt = null;
            pullRequestReviewRepository.persist(testReview).await().atMost(TIMEOUT);

            PullRequestReviewEntity pendingReview = pullRequestReviewRepository.findById(testReview.id())
                                                                               .await()
                                                                               .atMost(TIMEOUT);
            assertEquals(PullRequestReviewState.PENDING, pendingReview.state(),
                    "New review should be pending");
            assertNull(pendingReview.submittedAt(),
                    "Pending review should not have submitted date");

            // Approve review
            testReview.state = PullRequestReviewState.APPROVED;
            testReview.submittedAt = LocalDate.now();
            testReview.body = "Looks good!";
            pullRequestReviewRepository.update(testReview).await().atMost(TIMEOUT);

            PullRequestReviewEntity approvedReview = pullRequestReviewRepository.findById(
                    testReview.id()).await().atMost(TIMEOUT);
            assertEquals(PullRequestReviewState.APPROVED, approvedReview.state(),
                    "Review should be approved");
            assertNotNull(approvedReview.submittedAt(),
                    "Approved review should have submitted date");

            // Change to changes requested
            testReview.state = PullRequestReviewState.CHANGES_REQUESTED;
            testReview.body = "Please make these changes";
            pullRequestReviewRepository.update(testReview).await().atMost(TIMEOUT);

            PullRequestReviewEntity changesReview = pullRequestReviewRepository.findById(testReview.id())
                                                                               .await()
                                                                               .atMost(TIMEOUT);
            assertEquals(PullRequestReviewState.CHANGES_REQUESTED, changesReview.state(),
                    "Review should request changes");
            assertEquals("Please make these changes", changesReview.body(),
                    "Should have updated review comment");
        }
    }

    // Helper methods
    private void persistTestReviews(ObjectId userId1, ObjectId userId2, ObjectId repoId1,
                                    ObjectId repoId2) {
        pullRequestReviewRepository.persist(createTestPullRequestReview(null, userId1, repoId1, Optional.of("1")))
                                   .await()
                                   .atMost(TIMEOUT);
        pullRequestReviewRepository.persist(createTestPullRequestReview(null, userId1, repoId2, Optional.of("2")))
                                   .await()
                                   .atMost(TIMEOUT);
        pullRequestReviewRepository.persist(createTestPullRequestReview(null, userId1, repoId2, Optional.of("3")))
                                   .await()
                                   .atMost(TIMEOUT);
        pullRequestReviewRepository.persist(createTestPullRequestReview(null, userId2, repoId1, Optional.of("4")))
                                   .await()
                                   .atMost(TIMEOUT);
        pullRequestReviewRepository.persist(createTestPullRequestReview(null, userId2, repoId2, Optional.of("5")))
                                   .await()
                                   .atMost(TIMEOUT);
    }

    private PullRequestReviewEntity createTestPullRequestReview(ObjectId id, ObjectId userId,
                                                                ObjectId repoId, Optional<String> suffix) {
        var github = new Github();
        github.id = "test-github-" + suffix.orElse("id");
        github.url = URI.create("www.github.com/test-github-" + suffix.orElse("id"));
        var pullRequestGithub = new Github();
        pullRequestGithub.id = "test-pr-github-id";
        var userWithLogin = new UserWithLogin("test-user");
        var nameWithOwner = new NameWithOwner("test-repo", "test-user");

        var review = new PullRequestReviewEntity();
        review.id = id;
        review.userId = userId;
        review.repositoryId = repoId;
        review.user = userWithLogin;
        review.repository = nameWithOwner;
        review.github = github;
        review.pullRequest = pullRequestGithub;
        review.createdAt = LocalDate.now();
        review.submittedAt = null;
        review.updatedAt = LocalDate.now();
        review.publishedAt = LocalDate.now();
        review.lastEditedAt = null;
        review.state = PullRequestReviewState.PENDING;
        review.body = "Test review body";
        review.comments = null;

        return review;
    }
}