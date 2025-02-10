package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.TestEntityCreators;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryRepository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserRepository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Label;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueState;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("Issue Repository Tests")
class IssueRepositoryTest {

    @Inject
    IssueRepository issueRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    RepositoryRepository repositoryRepository;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    private UserEntity testUser;
    private RepositoryEntity testRepo;

    @BeforeEach
    @RunOnVertxContext
    void setUp(UniAsserter asserter) {
        // Create test user and repository
        testUser = TestEntityCreators.createUser("testIssueCreator");
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
                issueRepository.deleteAll()
                        .chain(() -> userRepository.deleteAll())
                        .chain(() -> repositoryRepository.deleteAll())));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @RunOnVertxContext
        @DisplayName("Should create a new issue successfully")
        void createIssue(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");

            asserter.assertThat(
                    () -> Panache.withTransaction(() -> issueRepository.persist(issue)),
                    persistedIssue -> {
                        assertNotNull(persistedIssue.getId());
                        assertEquals(issue.getGithubId(), persistedIssue.getGithubId());
                        assertEquals(issue.getTitle(), persistedIssue.getTitle());
                        assertEquals(IssueState.OPEN, persistedIssue.getState());
                        assertEquals(0, persistedIssue.getReactionsCount());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issue by ID")
        void findIssueById(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue)));

            asserter.assertThat(
                    () -> issueRepository.findById(issue.getId()),
                    foundIssue -> {
                        assertNotNull(foundIssue);
                        assertEquals(issue.getGithubId(), foundIssue.getGithubId());
                        assertEquals(issue.getTitle(), foundIssue.getTitle());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update existing issue")
        void updateIssue(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue)));

            // Verify initial state
            asserter.assertThat(
                    () -> issueRepository.findById(issue.getId()),
                    foundIssue -> {
                        assertEquals(IssueState.OPEN, foundIssue.getState());
                        assertEquals(0, foundIssue.getReactionsCount());
                        assertNull(foundIssue.getCloser());
                    }
                               );

            // Update issue
            asserter.execute(() -> {
                issue.setState(IssueState.CLOSED);
                issue.setReactionsCount(5);
                issue.setCloser("testCloser");
                issue.setClosedAt(LocalDateTime.now());
                return Panache.withTransaction(() -> issueRepository.persist(issue));
            });

            // Verify updates
            asserter.assertThat(
                    () -> issueRepository.findById(issue.getId()),
                    updatedIssue -> {
                        assertEquals(IssueState.CLOSED, updatedIssue.getState());
                        assertEquals(5, updatedIssue.getReactionsCount());
                        assertEquals("testCloser", updatedIssue.getCloser());
                        assertNotNull(updatedIssue.getClosedAt());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete existing issue")
        void deleteIssue(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue)));
            asserter.execute(() -> Panache.withTransaction(() ->
                    issueRepository.deleteById(issue.getId())));

            asserter.assertThat(
                    () -> issueRepository.findById(issue.getId()),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Label Management")
    class LabelManagementTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should add labels to issue")
        void addLabels(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");
            List<Label> labels = TestEntityCreators.createLabels(3);

            issue.setLabels(labels);

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue)));

            asserter.assertThat(
                    () -> issueRepository.findById(issue.getId()),
                    foundIssue -> {
                        assertEquals(3, foundIssue.getLabels().size());
                        assertTrue(foundIssue.getLabels().stream()
                                             .anyMatch(l -> l.getLabel().equals("label-0")));
                        assertTrue(foundIssue.getLabels().stream()
                                             .anyMatch(l -> l.getLabel().equals("label-1")));
                        assertTrue(foundIssue.getLabels().stream()
                                             .anyMatch(l -> l.getLabel().equals("label-2")));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update issue labels")
        void updateLabels(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");

            // Create initial labels with "initial-" prefix
            List<Label> initialLabels = TestEntityCreators.createLabels(2);
            issue.setLabels(initialLabels);

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue)));

            // Verify initial labels
            asserter.assertThat(
                    () -> issueRepository.findById(issue.getId()),
                    foundIssue -> {
                        assertEquals(2, foundIssue.getLabels().size());
                        assertTrue(foundIssue.getLabels().stream()
                                             .allMatch(l -> l.getLabel().startsWith("label-")));
                    }
                               );

            // Update labels
            asserter.execute(() -> {
                List<Label> newLabels = new ArrayList<>();
                newLabels.add(TestEntityCreators.createLabel("bug"));
                newLabels.add(TestEntityCreators.createLabel("enhancement"));
                newLabels.add(TestEntityCreators.createLabel("documentation"));
                issue.setLabels(newLabels);
                return Panache.withTransaction(() -> issueRepository.persist(issue));
            });

            // Verify new labels
            asserter.assertThat(
                    () -> issueRepository.findById(issue.getId()),
                    foundIssue -> {
                        assertEquals(3, foundIssue.getLabels().size());
                        assertTrue(foundIssue.getLabels().stream()
                                             .anyMatch(l -> l.getLabel().equals("bug")));
                        assertTrue(foundIssue.getLabels().stream()
                                             .anyMatch(l -> l.getLabel().equals("enhancement")));
                        assertTrue(foundIssue.getLabels().stream()
                                             .anyMatch(l -> l.getLabel().equals("documentation")));
                        assertFalse(foundIssue.getLabels().stream()
                                              .anyMatch(l -> l.getLabel().startsWith("label-")));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should cascade delete labels when issue is deleted")
        void cascadeDeleteLabels(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");
            List<Label> labels = TestEntityCreators.createLabels(2);
            issue.setLabels(labels);

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue)));

            // Verify labels exist
            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(l) FROM Label l", Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(2L, count)
                               );

            // Delete issue
            asserter.execute(() -> Panache.withTransaction(() ->
                    issueRepository.deleteById(issue.getId())));

            // Verify labels are deleted
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
    @DisplayName("Finder Methods")
    class FinderMethodTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issue by GitHub ID")
        void findByGitHubId(UniAsserter asserter) {
            IssueEntity issue = TestEntityCreators.createIssue(testUser, testRepo, "1");

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue)));

            asserter.assertThat(
                    () -> issueRepository.findByGitHubId(issue.getGithubId()),
                    foundIssue -> {
                        assertNotNull(foundIssue);
                        assertEquals(issue.getGithubId(), foundIssue.getGithubId());
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> issueRepository.findByGitHubId("nonexistent"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issues by user ID")
        void findByUserId(UniAsserter asserter) {
            List<IssueEntity> issues = TestEntityCreators.createIssues(testUser, testRepo, 3);

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issues)));

            asserter.assertThat(
                    () -> issueRepository.findByUserId(testUser.getId()),
                    foundIssues -> {
                        assertEquals(3, foundIssues.size());
                        assertTrue(foundIssues.stream().allMatch(issue ->
                                issue.getUser().getId().equals(testUser.getId())));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issues by repository ID")
        void findByRepoId(UniAsserter asserter) {
            List<IssueEntity> issues = TestEntityCreators.createIssues(testUser, testRepo, 3);

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issues)));

            asserter.assertThat(
                    () -> issueRepository.findByRepoId(testRepo.getId()),
                    foundIssues -> {
                        assertEquals(3, foundIssues.size());
                        assertTrue(foundIssues.stream().allMatch(issue ->
                                issue.getRepository().getId().equals(testRepo.getId())));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issues by user ID and repository ID")
        void findByUserIdAndRepoId(UniAsserter asserter) {
            List<IssueEntity> issues = TestEntityCreators.createIssues(testUser, testRepo, 3);

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issues)));

            asserter.assertThat(
                    () -> issueRepository.findByUserIdAndRepoId(testUser.getId(), testRepo.getId()),
                    foundIssues -> {
                        assertEquals(3, foundIssues.size());
                        assertTrue(foundIssues.stream().allMatch(issue ->
                                issue.getUser().getId().equals(testUser.getId()) &&
                                issue.getRepository().getId().equals(testRepo.getId())));
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
            IssueEntity issueWithNullUser = TestEntityCreators.createIssue(testUser, testRepo,
                    "null-user");
            issueWithNullUser.setUser(null);

            // Test with null repository
            IssueEntity issueWithNullRepo = TestEntityCreators.createIssue(testUser, testRepo,
                    "null-repo");
            issueWithNullRepo.setRepository(null);

            // Test with null githubId and githubUrl
            IssueEntity issueWithNullGithubId = TestEntityCreators.createIssue(testUser, testRepo,
                    "null-github-id");
            issueWithNullGithubId.setGithubId(null);
            issueWithNullGithubId.setGithubUrl(null);

            // Test with null created date
            IssueEntity issueWithNullCreatedAt = TestEntityCreators.createIssue(testUser, testRepo,
                    "null-created-at");
            issueWithNullCreatedAt.setCreatedAt(null);

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> issueRepository.persist(issueWithNullUser)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> issueRepository.persist(issueWithNullRepo)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> issueRepository.persist(issueWithNullGithubId)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> issueRepository.persist(issueWithNullCreatedAt)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should enforce unique constraints")
        void validateUniqueConstraints(UniAsserter asserter) {
            IssueEntity issue1 = TestEntityCreators.createIssue(testUser, testRepo, "1");
            IssueEntity issue2 = TestEntityCreators.createIssue(testUser, testRepo, "2");
            // Set same githubId and githubUrl for both issues
            issue2.setGithubId(issue1.getGithubId());
            issue2.setGithubUrl(issue1.getGithubUrl());

            asserter.execute(() -> Panache.withTransaction(() -> issueRepository.persist(issue1)));

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> issueRepository.persist(issue2)),
                    throwable -> assertTrue(
                            throwable.getMessage().toLowerCase().contains("constraint"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }
}