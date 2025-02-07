package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.TestEntityCreators;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryRepository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.User;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserRepository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.PullRequestReviewComment;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestReviewState;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("Pull Request Review Repository Tests")
class PullRequestReviewRepositoryTest {

    @Inject
    PullRequestReviewRepository reviewRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    RepositoryRepository repositoryRepository;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    private User testUser;
    private Repository testRepo;

    @BeforeEach
    @RunOnVertxContext
    void setUp(UniAsserter asserter) {
        // Create test user and repository
        testUser = TestEntityCreators.createUser("testReviewer");
        testRepo = TestEntityCreators.createRepository("testRepo", "testOwner");

        // Persist test entities
        asserter.execute(() -> Panache.withTransaction(() ->
                userRepository.persist(testUser)
                              .chain(() -> repositoryRepository.persist(testRepo))));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @AfterEach
    @RunOnVertxContext
    void tearDown(UniAsserter asserter) {
        // Clean up existing data
        asserter.execute(() -> Panache.withTransaction(() ->
                reviewRepository.deleteAll()
                                .chain(() -> userRepository.deleteAll())
                                .chain(() -> repositoryRepository.deleteAll())));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @RunOnVertxContext
        @DisplayName("Should create a new review successfully")
        void createReview(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");

            asserter.assertThat(
                    () -> Panache.withTransaction(() -> reviewRepository.persist(review)),
                    persistedReview -> {
                        assertNotNull(persistedReview.getId());
                        assertEquals(review.getGithubId(), persistedReview.getGithubId());
                        assertEquals(PullRequestReviewState.PENDING, persistedReview.getState());
                        assertNotNull(persistedReview.getCreatedAt());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find review by ID")
        void findReviewById(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> reviewRepository.persist(review)));

            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    foundReview -> {
                        assertNotNull(foundReview);
                        assertEquals(review.getGithubId(), foundReview.getGithubId());
                        assertEquals(review.getState(), foundReview.getState());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update existing review")
        void updateReview(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> reviewRepository.persist(review)));

            // Verify initial state
            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    foundReview -> {
                        assertEquals(PullRequestReviewState.PENDING, foundReview.getState());
                        assertNull(foundReview.getSubmittedAt());
                    }
                               );

            // Update review
            asserter.execute(() -> {
                review.setState(PullRequestReviewState.APPROVED);
                review.setSubmittedAt(LocalDateTime.now());
                review.setBody("Updated review body");
                return Panache.withTransaction(() -> reviewRepository.persist(review));
            });

            // Verify updates
            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    updatedReview -> {
                        assertEquals(PullRequestReviewState.APPROVED, updatedReview.getState());
                        assertNotNull(updatedReview.getSubmittedAt());
                        assertEquals("Updated review body", updatedReview.getBody());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete existing review")
        void deleteReview(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> reviewRepository.persist(review)));
            asserter.execute(() -> Panache.withTransaction(() ->
                    reviewRepository.deleteById(review.getId())));

            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Review Comments Management")
    class ReviewCommentsTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should manage review comments")
        void manageReviewComments(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");
            List<PullRequestReviewComment> comments =
                    TestEntityCreators.createPullRequestReviewComments(
                            3);

            review.setComments(comments);

            asserter.execute(() -> Panache.withTransaction(() -> reviewRepository.persist(review)));

            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    foundReview -> {
                        assertEquals(3, foundReview.getComments().size());
                        assertTrue(foundReview.getComments().stream()
                                              .anyMatch(c -> c.getLogin().equals("testReviewer0")));
                        assertTrue(foundReview.getComments().stream()
                                              .anyMatch(c -> c.getLogin().equals("testReviewer1")));
                        assertTrue(foundReview.getComments().stream()
                                              .anyMatch(c -> c.getLogin().equals("testReviewer2")));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should cascade delete review comments")
        void cascadeDeleteComments(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");
            List<PullRequestReviewComment> comments =
                    TestEntityCreators.createPullRequestReviewComments(
                            2);
            review.setComments(comments);

            asserter.execute(() -> Panache.withTransaction(() -> reviewRepository.persist(review)));

            // Verify comments exist
            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(c) " +
                                                        "FROM PullRequestReviewComment c",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(2L, count)
                               );

            // Delete review
            asserter.execute(() -> Panache.withTransaction(() ->
                    reviewRepository.deleteById(review.getId())));

            // Verify comments are deleted
            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(c) " +
                                                        "FROM PullRequestReviewComment c",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Finder Methods")
    class FinderMethodTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should find review by GitHub ID")
        void findByGitHubId(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> reviewRepository.persist(review)));

            asserter.assertThat(
                    () -> reviewRepository.findByGitHubId(review.getGithubId()),
                    foundReview -> {
                        assertNotNull(foundReview);
                        assertEquals(review.getGithubId(), foundReview.getGithubId());
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> reviewRepository.findByGitHubId("nonexistent"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find reviews by user ID")
        void findByUserId(UniAsserter asserter) {
            List<PullRequestReview> reviews = TestEntityCreators.createPullRequestReviews(testUser,
                    testRepo, 3);

            asserter.execute(
                    () -> Panache.withTransaction(() -> reviewRepository.persist(reviews)));

            asserter.assertThat(
                    () -> reviewRepository.findByUserId(testUser.getId()),
                    foundReviews -> {
                        assertEquals(3, foundReviews.size());
                        assertTrue(foundReviews.stream()
                                               .allMatch(review -> review.getUser()
                                                                         .getId()
                                                                         .equals(testUser.getId())));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find reviews by repository ID")
        void findByRepoId(UniAsserter asserter) {
            List<PullRequestReview> reviews = TestEntityCreators.createPullRequestReviews(testUser,
                    testRepo, 3);

            asserter.execute(
                    () -> Panache.withTransaction(() -> reviewRepository.persist(reviews)));

            asserter.assertThat(
                    () -> reviewRepository.findByRepoId(testRepo.getId()),
                    foundReviews -> {
                        assertEquals(3, foundReviews.size());
                        assertTrue(foundReviews.stream()
                                               .allMatch(review -> review.getRepository()
                                                                         .getId()
                                                                         .equals(testRepo.getId())));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find reviews by user ID and repository ID")
        void findByUserIdAndRepoId(UniAsserter asserter) {
            List<PullRequestReview> reviews = TestEntityCreators.createPullRequestReviews(testUser,
                    testRepo, 3);

            asserter.execute(
                    () -> Panache.withTransaction(() -> reviewRepository.persist(reviews)));

            asserter.assertThat(
                    () -> reviewRepository.findByUserIdAndRepoId(testUser.getId(),
                            testRepo.getId()),
                    foundReviews -> {
                        assertEquals(3, foundReviews.size());
                        assertTrue(foundReviews.stream()
                                               .allMatch(review ->
                                                               review.getUser()
                                                                     .getId()
                                                                     .equals(testUser.getId()) &&
                                                               review.getRepository()
                                                                     .getId()
                                                                     .equals(testRepo.getId())
                                                        ));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should fail when required fields are null")
        void validateRequiredFields(UniAsserter asserter) {
            // Test with null user
            PullRequestReview reviewWithNullUser = TestEntityCreators.createPullRequestReview(
                    testUser, testRepo, "null-user");
            reviewWithNullUser.setUser(null);

            // Test with null repository
            PullRequestReview reviewWithNullRepo = TestEntityCreators.createPullRequestReview(
                    testUser, testRepo, "null-repo");
            reviewWithNullRepo.setRepository(null);

            // Test with null githubId and githubUrl
            PullRequestReview reviewWithNullGithubId = TestEntityCreators.createPullRequestReview(
                    testUser, testRepo, "null-github-id");
            reviewWithNullGithubId.setGithubId(null);
            reviewWithNullGithubId.setGithubUrl(null);

            // Test with null createdAt
            PullRequestReview reviewWithNullCreatedAt = TestEntityCreators.createPullRequestReview(
                    testUser, testRepo, "null-created-at");
            reviewWithNullCreatedAt.setCreatedAt(null);

            // Test with null pullRequest
            PullRequestReview reviewWithNullPullRequest =
                    TestEntityCreators.createPullRequestReview(
                            testUser, testRepo, "null-pull-request");
            reviewWithNullPullRequest.setPullRequest(null);

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> reviewRepository.persist(reviewWithNullUser)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> reviewRepository.persist(reviewWithNullRepo)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> reviewRepository.persist(reviewWithNullGithubId)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> reviewRepository.persist(reviewWithNullCreatedAt)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> reviewRepository.persist(reviewWithNullPullRequest)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should enforce unique constraints")
        void validateUniqueConstraints(UniAsserter asserter) {
            PullRequestReview review1 = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");
            PullRequestReview review2 = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "2");

            // Set same githubId and githubUrl for both reviews
            review2.setGithubId(review1.getGithubId());
            review2.setGithubUrl(review1.getGithubUrl());

            asserter.execute(
                    () -> Panache.withTransaction(() -> reviewRepository.persist(review1)));

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> reviewRepository.persist(review2)),
                    throwable -> assertTrue(
                            throwable.getMessage().toLowerCase().contains("constraint"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle review state transitions")
        void handleStateTransitions(UniAsserter asserter) {
            PullRequestReview review = TestEntityCreators.createPullRequestReview(testUser,
                    testRepo, "1");

            // Initial state should be PENDING
            asserter.execute(() -> Panache.withTransaction(() -> reviewRepository.persist(review)));

            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    foundReview -> assertEquals(PullRequestReviewState.PENDING,
                            foundReview.getState())
                               );

            // Update to APPROVED
            asserter.execute(() -> {
                review.setState(PullRequestReviewState.APPROVED);
                review.setSubmittedAt(LocalDateTime.now());
                return Panache.withTransaction(() -> reviewRepository.persist(review));
            });

            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    foundReview -> {
                        assertEquals(PullRequestReviewState.APPROVED, foundReview.getState());
                        assertNotNull(foundReview.getSubmittedAt());
                    }
                               );

            // Update to CHANGES_REQUESTED
            asserter.execute(() -> {
                review.setState(PullRequestReviewState.CHANGES_REQUESTED);
                review.setBody("Please make these changes");
                return Panache.withTransaction(() -> reviewRepository.persist(review));
            });

            asserter.assertThat(
                    () -> reviewRepository.findById(review.getId()),
                    foundReview -> {
                        assertEquals(PullRequestReviewState.CHANGES_REQUESTED,
                                foundReview.getState());
                        assertEquals("Please make these changes", foundReview.getBody());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }
}