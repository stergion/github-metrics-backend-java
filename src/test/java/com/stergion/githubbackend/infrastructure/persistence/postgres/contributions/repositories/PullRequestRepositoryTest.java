package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.TestEntityCreators;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryRepository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.User;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserRepository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.ClosingIssuesReference;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Label;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.PullRequestCommit;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestState;
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
@DisplayName("Pull Request Repository Tests")
class PullRequestRepositoryTest {

    @Inject
    PullRequestRepository pullRequestRepository;

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
        testUser = TestEntityCreators.createUser("testPRCreator");
        testRepo = TestEntityCreators.createRepository("testRepo", "testOwner");

        asserter.execute(() -> Panache.withTransaction(() ->
                userRepository.persist(testUser)
                              .chain(() -> repositoryRepository.persist(testRepo))));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @AfterEach
    @RunOnVertxContext
    void tearDown(UniAsserter asserter) {
        asserter.execute(() -> Panache.withTransaction(() ->
                pullRequestRepository.deleteAll()
                                     .chain(() -> userRepository.deleteAll())
                                     .chain(() -> repositoryRepository.deleteAll())));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @RunOnVertxContext
        @DisplayName("Should create a new pull request successfully")
        void createPullRequest(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");

            asserter.assertThat(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)),
                    persistedPr -> {
                        assertNotNull(persistedPr.getId());
                        assertEquals(pr.getGithubId(), persistedPr.getGithubId());
                        assertEquals(pr.getTitle(), persistedPr.getTitle());
                        assertEquals(PullRequestState.OPEN, persistedPr.getState());
                        assertEquals(0, persistedPr.getReactionsCount());
                        assertNotNull(persistedPr.getCreatedAt());
                        assertNull(persistedPr.getMergedAt());
                        assertNull(persistedPr.getClosedAt());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle pull request state transitions")
        void handleStateTransitions(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");

            // Initial state: OPEN
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)));

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    foundPr -> {
                        assertEquals(PullRequestState.OPEN, foundPr.getState());
                        assertNull(foundPr.getClosedAt());
                        assertNull(foundPr.getMergedAt());
                    }
                               );

            // Transition to CLOSED
            LocalDateTime closedAt = LocalDateTime.now();
            asserter.execute(() -> {
                pr.setState(PullRequestState.CLOSED);
                pr.setClosedAt(closedAt);
                return Panache.withTransaction(() -> pullRequestRepository.persist(pr));
            });

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    foundPr -> {
                        assertEquals(PullRequestState.CLOSED, foundPr.getState());
                        assertNotNull(foundPr.getClosedAt());
                        assertNull(foundPr.getMergedAt());
                    }
                               );

            // Transition to MERGED
            LocalDateTime mergedAt = LocalDateTime.now()
                                                  .plusSeconds(1); // Ensure it's after closedAt
            asserter.execute(() -> {
                pr.setState(PullRequestState.MERGED);
                pr.setMergedAt(mergedAt);
                return Panache.withTransaction(() -> pullRequestRepository.persist(pr));
            });

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    foundPr -> {
                        assertEquals(PullRequestState.MERGED, foundPr.getState());
                        assertNotNull(foundPr.getClosedAt());
                        assertNotNull(foundPr.getMergedAt());
                        assertTrue(foundPr.getMergedAt().isAfter(foundPr.getClosedAt()));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update pull request content")
        void updateContent(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)));

            String newTitle = "Updated PR Title";
            String newBody = "Updated PR description";
            int newReactionsCount = 5;

            asserter.execute(() -> {
                pr.setTitle(newTitle);
                pr.setBody(newBody);
                pr.setReactionsCount(newReactionsCount);
                pr.setUpdatedAt(LocalDateTime.now());
                return Panache.withTransaction(() -> pullRequestRepository.persist(pr));
            });

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    updatedPr -> {
                        assertEquals(newTitle, updatedPr.getTitle());
                        assertEquals(newBody, updatedPr.getBody());
                        assertEquals(newReactionsCount, updatedPr.getReactionsCount());
                        assertNotNull(updatedPr.getUpdatedAt());
                        assertTrue(updatedPr.getUpdatedAt().isAfter(updatedPr.getCreatedAt()));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete pull request and cascade relationships")
        void deletePullRequest(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");

            // Add relationships
            pr.setLabels(TestEntityCreators.createLabels(2));
            pr.setCommits(TestEntityCreators.createPullRequestCommits(2));
            pr.setClosingIssuesReferences(TestEntityCreators.createClosingIssuesReferences(2));

            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)));

            // Verify relationships exist
            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(l) FROM Label l", Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(2L, count)
                               );

            // Delete PR
            asserter.execute(() -> Panache.withTransaction(() ->
                            pullRequestRepository.deleteById(pr.getId())
                                                          ));

            // Verify PR and relationships are deleted
            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    Assertions::assertNull
                               );

            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(l) FROM Label l", Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Relationship Management")
    class RelationshipTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should manage labels")
        void manageLabels(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");
            List<Label> labels = TestEntityCreators.createLabels(3);

            pr.setLabels(labels);
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)));

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    foundPr -> {
                        assertEquals(3, foundPr.getLabels().size());
                        assertTrue(foundPr.getLabels().stream()
                                          .anyMatch(l -> l.getLabel().equals("label-0")));
                    }
                               );

            // Update labels
            asserter.execute(() -> {
                List<Label> newLabels = TestEntityCreators.createLabels(2);
                pr.setLabels(newLabels);
                return Panache.withTransaction(() -> pullRequestRepository.persist(pr));
            });

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    foundPr -> {
                        assertEquals(2, foundPr.getLabels().size());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should manage commits")
        void manageCommits(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");
            List<PullRequestCommit> commits = TestEntityCreators.createPullRequestCommits(3);

            pr.setCommits(commits);
            pr.setCommitsCount(commits.size());
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)));

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    foundPr -> {
                        assertEquals(3, foundPr.getCommitsCount());
                        assertEquals(3, foundPr.getCommits().size());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should manage closing issues references")
        void manageClosingIssues(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");
            List<ClosingIssuesReference> refs = TestEntityCreators.createClosingIssuesReferences(2);

            pr.setClosingIssuesReferences(refs);
            pr.setClosingIssuesReferencesCount(refs.size());
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)));

            asserter.assertThat(
                    () -> pullRequestRepository.findById(pr.getId()),
                    foundPr -> {
                        assertEquals(2, foundPr.getClosingIssuesReferencesCount());
                        assertEquals(2, foundPr.getClosingIssuesReferences().size());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Finder Methods")
    class FinderTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should find by GitHub ID")
        void findByGitHubId(UniAsserter asserter) {
            PullRequest pr = TestEntityCreators.createPullRequest(testUser, testRepo, "1");
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr)));

            asserter.assertThat(
                    () -> pullRequestRepository.findByGitHubId(pr.getGithubId()),
                    foundPr -> {
                        assertNotNull(foundPr);
                        assertEquals(pr.getGithubId(), foundPr.getGithubId());
                    }
                               );

            asserter.assertThat(
                    () -> pullRequestRepository.findByGitHubId("nonexistent"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find by repository and user")
        void findByRepoAndUser(UniAsserter asserter) {
            List<PullRequest> prs = TestEntityCreators.createPullRequests(testUser, testRepo, 3);
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(prs)));

            asserter.assertThat(
                    () -> pullRequestRepository.findByUserIdAndRepoId(testUser.getId(),
                            testRepo.getId()),
                    foundPrs -> {
                        assertEquals(3, foundPrs.size());
                        assertTrue(foundPrs.stream()
                                           .allMatch(pr ->
                                                           pr.getUser().getId().equals(testUser.getId()) &&
                                                           pr.getRepository()
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
        @DisplayName("Should enforce required fields")
        void enforceRequiredFields(UniAsserter asserter) {
            // Test with null user
            PullRequest prWithNullUser = TestEntityCreators.createPullRequest(testUser, testRepo,
                    "null-user");
            prWithNullUser.setUser(null);

            // Test with null repository
            PullRequest prWithNullRepo = TestEntityCreators.createPullRequest(testUser, testRepo,
                    "null-repo");
            prWithNullRepo.setRepository(null);

            // Test with null createdAt
            PullRequest prWithNullCreatedAt = TestEntityCreators.createPullRequest(testUser,
                    testRepo, "null-created");
            prWithNullCreatedAt.setCreatedAt(null);

            // Test with null githubId
            PullRequest prWithNullGithubId = TestEntityCreators.createPullRequest(testUser,
                    testRepo, "null-github");
            prWithNullGithubId.setGithubId(null);

            // Test with null githubUrl
            PullRequest prWithNullGithubUrl = TestEntityCreators.createPullRequest(testUser,
                    testRepo, "null-url");
            prWithNullGithubUrl.setGithubUrl(null);

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> pullRequestRepository.persist(prWithNullUser)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> pullRequestRepository.persist(prWithNullRepo)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> pullRequestRepository.persist(prWithNullCreatedAt)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> pullRequestRepository.persist(prWithNullGithubId)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> pullRequestRepository.persist(prWithNullGithubUrl)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should enforce unique constraints")
        void enforceUniqueConstraints(UniAsserter asserter) {
            PullRequest pr1 = TestEntityCreators.createPullRequest(testUser, testRepo, "1");
            PullRequest pr2 = TestEntityCreators.createPullRequest(testUser, testRepo, "2");
            // Set same githubId for both pull requests
            pr2.setGithubId(pr1.getGithubId());
            pr2.setGithubUrl(pr1.getGithubUrl());

            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr1)));

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(pr2)),
                    throwable -> assertTrue(
                            throwable.getMessage().toLowerCase().contains("constraint"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Bulk Operations")
    class BulkOperationTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle bulk deletions")
        void handleBulkDeletions(UniAsserter asserter) {
            // Create multiple pull requests
            List<PullRequest> prs = TestEntityCreators.createPullRequests(testUser, testRepo, 5);
            for (PullRequest pr : prs) {
                pr.setLabels(TestEntityCreators.createLabels(2));
                pr.setCommits(TestEntityCreators.createPullRequestCommits(2));
                pr.setClosingIssuesReferences(TestEntityCreators.createClosingIssuesReferences(1));
            }

            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.persist(prs)));

            // Verify all entities exist
            asserter.assertThat(
                    () -> pullRequestRepository.count(),
                    count -> assertEquals(5L, count)
                               );

            // Delete all
            asserter.execute(
                    () -> Panache.withTransaction(() -> pullRequestRepository.deleteAll()));

            // Verify all entities are deleted
            asserter.assertThat(
                    () -> pullRequestRepository.count(),
                    count -> assertEquals(0L, count)
                               );

            // Verify related entities are deleted
            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(l) FROM Label l", Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(c) FROM PullRequestCommit c",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(r) FROM " +
                                                        "ClosingIssuesReference r",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

}