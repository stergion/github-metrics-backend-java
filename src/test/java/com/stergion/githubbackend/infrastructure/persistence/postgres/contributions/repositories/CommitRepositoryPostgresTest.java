package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.TestEntityCreators;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.CommitEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryRepositoryPostgres;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserRepositoryPostgres;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.AssociatedPullRequest;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.CommitComment;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.File;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("Commit Repository Tests")
class CommitRepositoryPostgresTest {

    @Inject
    CommitRepositoryPostgres commitRepository;

    @Inject
    UserRepositoryPostgres userRepository;

    @Inject
    RepositoryRepositoryPostgres repositoryRepository;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    private UserEntity testUser;
    private RepositoryEntity testRepo;

    @BeforeEach
    @RunOnVertxContext
    void setUp(UniAsserter asserter) {
        // Create test user and repository
        testUser = TestEntityCreators.createUser("testCommitter");
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
                commitRepository.deleteAll()
                                .chain(() -> userRepository.deleteAll())
                                .chain(() -> repositoryRepository.deleteAll())));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @RunOnVertxContext
        @DisplayName("Should create a new commit successfully")
        void createCommit(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            asserter.assertThat(
                    () -> Panache.withTransaction(() -> commitRepository.persist(commit)),
                    persistedCommit -> {
                        assertNotNull(persistedCommit.getId());
                        assertEquals(commit.getGithubId(), persistedCommit.getGithubId());
                        assertEquals(10, persistedCommit.getAdditions());
                        assertEquals(5, persistedCommit.getDeletions());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find commit by ID")
        void findCommitById(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            asserter.execute(() -> Panache.withTransaction(() -> commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    foundCommit -> {
                        assertNotNull(foundCommit);
                        assertEquals(commit.getGithubId(), foundCommit.getGithubId());
                        assertEquals(commit.getAdditions(), foundCommit.getAdditions());
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update existing commit")
        void updateCommit(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            asserter.execute(() -> Panache.withTransaction(() -> commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    updatedCommit -> {
                        assertNotEquals(20, updatedCommit.getAdditions());
                        assertNotEquals(10, updatedCommit.getDeletions());
                    }
                               );

            asserter.execute(() -> {
                commit.setAdditions(20);
                commit.setDeletions(10);
                return Panache.withTransaction(() -> commitRepository.persist(commit));
            });

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    updatedCommit -> {
                        assertEquals(20, updatedCommit.getAdditions());
                        assertEquals(10, updatedCommit.getDeletions());
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete existing commit")
        void deleteCommit(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            asserter.execute(() -> Panache.withTransaction(() -> commitRepository.persist(commit)));
            asserter.execute(() -> Panache.withTransaction(() ->
                    commitRepository.deleteById(commit.getId())));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    Assertions::assertNull
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Finder Methods")
    class FinderMethodTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should find commit by GitHub ID")
        void findByGitHubId(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            asserter.execute(() -> Panache.withTransaction(() -> commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findByGitHubId(commit.getGithubId()),
                    foundCommit -> {
                        assertNotNull(foundCommit);
                        assertEquals(commit.getGithubId(), foundCommit.getGithubId());
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> commitRepository.findByGitHubId("nonexistent"),
                    Assertions::assertNull
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find commits by user ID")
        void findByUserId(UniAsserter asserter) {
            List<CommitEntity> commits = TestEntityCreators.createCommits(testUser, testRepo, 3);

            asserter.execute(() -> Panache.withTransaction(() ->
                    commitRepository.persist(commits)));

            asserter.assertThat(
                    () -> commitRepository.findByUserId(testUser.getId()),
                    foundCommits -> {
                        assertEquals(3, foundCommits.size());
                        assertTrue(foundCommits.stream()
                                               .allMatch(c -> c.getUser()
                                                               .getId()
                                                               .equals(testUser.getId())));
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find commits by repository ID")
        void findByRepoId(UniAsserter asserter) {
            List<CommitEntity> commits = TestEntityCreators.createCommits(testUser, testRepo, 3);

            asserter.execute(() -> Panache.withTransaction(() ->
                    commitRepository.persist(commits)));

            asserter.assertThat(
                    () -> commitRepository.findByRepoId(testRepo.getId()),
                    foundCommits -> {
                        assertEquals(3, foundCommits.size());
                        assertTrue(foundCommits.stream()
                                               .allMatch(c -> c.getRepository()
                                                               .getId()
                                                               .equals(testRepo.getId())));
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find commits by user ID and repository ID")
        void findByUserIdAndRepoId(UniAsserter asserter) {
            List<CommitEntity> commits = TestEntityCreators.createCommits(testUser, testRepo, 3);

            asserter.execute(() -> Panache.withTransaction(() ->
                    commitRepository.persist(commits)));

            asserter.assertThat(
                    () -> commitRepository.findByUserIdAndRepoId(testUser.getId(),
                            testRepo.getId()),
                    foundCommits -> {
                        assertEquals(3, foundCommits.size());
                        assertTrue(foundCommits.stream().allMatch(c ->
                                        c.getUser().getId().equals(testUser.getId()) &&
                                        c.getRepository().getId().equals(testRepo.getId())
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
            var commit = TestEntityCreators.createCommit(testUser, testRepo);
            commit.setUser(null); // Required field

            var commit2 = TestEntityCreators.createCommit(testUser, testRepo);
            commit2.setGithubId(null);

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> commitRepository.persist(commit)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> commitRepository.persist(commit2)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should enforce unique constraints")
        void validateUniqueConstraints(UniAsserter asserter) {
            CommitEntity commit1 = TestEntityCreators.createCommit(testUser, testRepo);
            CommitEntity commit2 = TestEntityCreators.createCommit(testUser, testRepo);
            // Same githubId
            commit2.setGithubId(commit1.getGithubId());

            asserter.execute(
                    () -> Panache.withTransaction(() -> commitRepository.persist(commit1)));

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> commitRepository.persist(commit2)),
                    throwable -> assertTrue(
                            throwable.getMessage().toLowerCase().contains("constraint"))
                                     );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should maintain correct count of relationships")
        void validateRelationshipCounts(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);
            List<CommitComment> comments = TestEntityCreators.createCommitComments(3);
            List<AssociatedPullRequest> prs = TestEntityCreators.createAssociatedPullRequests(
                    2);

            commit.setComments(comments);
            commit.setCommentsCount(999); // Intentionally wrong count
            commit.setAssociatedPullRequest(prs);
            commit.setAssociatedPullRequestsCount(888); // Intentionally wrong count

            asserter.execute(() -> Panache.withTransaction(() -> commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    foundCommit -> {
                        // Verify actual counts don't match declared counts
                        assertNotEquals(foundCommit.getCommentsCount(),
                                foundCommit.getComments().size());
                        assertNotEquals(
                                foundCommit.getAssociatedPullRequestsCount(),
                                foundCommit.getAssociatedPullRequest().size()
                                       );
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("File Tests")
    class FileTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle commit with files")
        void commitWithFiles(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);
            List<File> files = TestEntityCreators.createFiles(3);

            commit.setFiles(files);
            commit.setFilesCount(files.size());

            // Set additions/deletions to match file totals
            int totalAdditions = files.stream().mapToInt(File::getAdditions).sum();
            int totalDeletions = files.stream().mapToInt(File::getDeletions).sum();
            commit.setAdditions(totalAdditions);
            commit.setDeletions(totalDeletions);

            asserter.execute(() -> Panache.withTransaction(() -> commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    foundCommit -> {
                        assertEquals(3, foundCommit.getFilesCount());
                        assertEquals(3, foundCommit.getFiles().size());
                        assertEquals(totalAdditions, foundCommit.getAdditions());
                        assertEquals(totalDeletions, foundCommit.getDeletions());
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle large commits")
        void handleLargeCommits(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);
            List<File> files = TestEntityCreators.createFiles(100); // Large number of files

            commit.setFiles(files);
            commit.setFilesCount(files.size());
            commit.setAdditions(10000); // Large number of additions
            commit.setDeletions(5000);  // Large number of deletions

            asserter.execute(() -> Panache.withTransaction(() -> commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    foundCommit -> {
                        assertEquals(100, foundCommit.getFiles().size());
                        assertEquals(10000, foundCommit.getAdditions());
                        assertEquals(5000, foundCommit.getDeletions());
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Relationship Tests")
    class RelationshipTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle commit with comments")
        void commitWithComments(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            List<CommitComment> comments = TestEntityCreators.createCommitComments(3);

            commit.setComments(comments);
            commit.setCommentsCount(comments.size());

            asserter.execute(() -> Panache.withTransaction(() ->
                    commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    foundCommit -> {
                        assertEquals(3, foundCommit.getCommentsCount());
                        assertEquals(3, foundCommit.getComments().size());
                        assertTrue(foundCommit.getComments().stream()
                                              .allMatch(c -> c.getAuthor().equals("testAuthor")));
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle commit with associated pull requests")
        void commitWithPullRequests(UniAsserter asserter) {
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            List<AssociatedPullRequest> prs = TestEntityCreators.createAssociatedPullRequests(2);

            commit.setAssociatedPullRequest(prs);
            commit.setAssociatedPullRequestsCount(prs.size());

            asserter.execute(() -> Panache.withTransaction(() ->
                    commitRepository.persist(commit)));

            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    foundCommit -> {
                        assertEquals(2, foundCommit.getAssociatedPullRequestsCount());
                        assertEquals(2, foundCommit.getAssociatedPullRequest().size());
                    }
                               );
            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should cascade delete commit relationships")
        void cascadeDeleteRelationships(UniAsserter asserter) {
            // Create commit with relationships
            CommitEntity commit = TestEntityCreators.createCommit(testUser, testRepo);

            // Add commit comments
            CommitComment comment = TestEntityCreators.createCommitComment();
            commit.setComments(List.of(comment));
            commit.setCommentsCount(1);

            // Add associated pull requests
            AssociatedPullRequest apr = TestEntityCreators.createAssociatedPullRequest();
            commit.setAssociatedPullRequest(List.of(apr));
            commit.setAssociatedPullRequestsCount(1);

            // Add files
            File file = TestEntityCreators.createFile();
            commit.setFiles(List.of(file));
            commit.setFilesCount(1);

            // First persist everything
            asserter.execute(() ->
                            Panache.withTransaction(() -> commitRepository.persist(commit))
                            );


            // Verify entities exist in their tables
            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(c) FROM CommitComment c",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(1L, count)
                               );

            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(p) FROM " +
                                                        "AssociatedPullRequest p",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(1L, count)
                               );

            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(f) FROM File f", Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(1L, count)
                               );

            // Delete the commit
            asserter.execute(() ->
                            Panache.withTransaction(() -> commitRepository.deleteById(commit.getId()))
                            );

            // Verify commit is deleted
            asserter.assertThat(
                    () -> commitRepository.findById(commit.getId()),
                    Assertions::assertNull
                               );

            // Verify cascade deletion of relationships
            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(c) FROM CommitComment c",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(p) FROM " +
                                                        "AssociatedPullRequest p",
                                                   Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.assertThat(
                    () -> sessionFactory.withSession(session ->
                                    session.createQuery("SELECT COUNT(f) FROM File f", Long.class)
                                           .getSingleResult()
                                                    ),
                    count -> assertEquals(0L, count)
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }
}