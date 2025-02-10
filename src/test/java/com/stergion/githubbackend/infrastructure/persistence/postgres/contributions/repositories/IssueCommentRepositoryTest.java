package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.TestEntityCreators;
import com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities.IssueComment;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryRepository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserEntity;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.UserRepository;
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
@DisplayName("Issue Comment Repository Tests")
class IssueCommentRepositoryTest {

    @Inject
    IssueCommentRepository issueCommentRepository;

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
        testUser = TestEntityCreators.createUser("testCommentCreator");
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
                issueCommentRepository.deleteAll()
                        .chain(() -> userRepository.deleteAll())
                        .chain(() -> repositoryRepository.deleteAll())));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @RunOnVertxContext
        @DisplayName("Should create a new issue comment successfully")
        void createIssueComment(UniAsserter asserter) {
            IssueComment comment = TestEntityCreators.createIssueComment(testUser, testRepo, "1");

            asserter.assertThat(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comment)),
                    persistedComment -> {
                        assertNotNull(persistedComment.getId());
                        assertEquals(comment.getGithubId(), persistedComment.getGithubId());
                        assertEquals(comment.getBody(), persistedComment.getBody());
                        assertNotNull(persistedComment.getCreatedAt());
                        assertNotNull(persistedComment.getPublishedAt());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issue comment by ID")
        void findIssueCommentById(UniAsserter asserter) {
            IssueComment comment = TestEntityCreators.createIssueComment(testUser, testRepo, "1");

            asserter.execute(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comment)));

            asserter.assertThat(
                    () -> issueCommentRepository.findById(comment.getId()),
                    foundComment -> {
                        assertNotNull(foundComment);
                        assertEquals(comment.getGithubId(), foundComment.getGithubId());
                        assertEquals(comment.getBody(), foundComment.getBody());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update existing issue comment")
        void updateIssueComment(UniAsserter asserter) {
            IssueComment comment = TestEntityCreators.createIssueComment(testUser, testRepo, "1");

            asserter.execute(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comment)));

            asserter.execute(() -> {
                comment.setBody("Updated body");
                comment.setLastEditedAt(LocalDateTime.now());
                return Panache.withTransaction(() -> issueCommentRepository.persist(comment));
            });

            asserter.assertThat(
                    () -> issueCommentRepository.findById(comment.getId()),
                    updatedComment -> {
                        assertEquals("Updated body", updatedComment.getBody());
                        assertNotNull(updatedComment.getLastEditedAt());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete existing issue comment")
        void deleteIssueComment(UniAsserter asserter) {
            IssueComment comment = TestEntityCreators.createIssueComment(testUser, testRepo, "1");

            asserter.execute(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comment)));
            asserter.execute(() -> Panache.withTransaction(() ->
                    issueCommentRepository.deleteById(comment.getId())));

            asserter.assertThat(
                    () -> issueCommentRepository.findById(comment.getId()),
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
        @DisplayName("Should find issue comment by GitHub ID")
        void findByGitHubId(UniAsserter asserter) {
            IssueComment comment = TestEntityCreators.createIssueComment(testUser, testRepo, "1");

            asserter.execute(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comment)));

            asserter.assertThat(
                    () -> issueCommentRepository.findByGitHubId(comment.getGithubId()),
                    foundComment -> {
                        assertNotNull(foundComment);
                        assertEquals(comment.getGithubId(), foundComment.getGithubId());
                    }
                               );

            asserter.assertThat(
                    () -> issueCommentRepository.findByGitHubId("nonexistent"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issue comments by user ID")
        void findByUserId(UniAsserter asserter) {
            List<IssueComment> comments = TestEntityCreators.createIssueComments(testUser, testRepo,
                    3);

            asserter.execute(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comments)));

            asserter.assertThat(
                    () -> issueCommentRepository.findByUserId(testUser.getId()),
                    foundComments -> {
                        assertEquals(3, foundComments.size());
                        assertTrue(foundComments.stream()
                                                .allMatch(comment -> comment.getUser()
                                                                            .getId()
                                                                            .equals(testUser.getId())));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find issue comments by repository ID")
        void findByRepoId(UniAsserter asserter) {
            List<IssueComment> comments = TestEntityCreators.createIssueComments(testUser, testRepo,
                    3);

            asserter.execute(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comments)));

            asserter.assertThat(
                    () -> issueCommentRepository.findByRepoId(testRepo.getId()),
                    foundComments -> {
                        assertEquals(3, foundComments.size());
                        assertTrue(foundComments.stream()
                                                .allMatch(comment -> comment.getRepository()
                                                                            .getId()
                                                                            .equals(testRepo.getId())));
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
            IssueComment commentWithNullUser = TestEntityCreators.createIssueComment(testUser,
                    testRepo, "null-user");
            commentWithNullUser.setUser(null);

            IssueComment commentWithNullRepo = TestEntityCreators.createIssueComment(testUser,
                    testRepo, "null-repo");
            commentWithNullRepo.setRepository(null);

            IssueComment commentWithNullCreatedAt = TestEntityCreators.createIssueComment(testUser,
                    testRepo, "null-created");
            commentWithNullCreatedAt.setCreatedAt(null);

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> issueCommentRepository.persist(commentWithNullUser)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> issueCommentRepository.persist(commentWithNullRepo)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(
                            () -> issueCommentRepository.persist(commentWithNullCreatedAt)),
                    throwable -> assertTrue(throwable.getMessage().toLowerCase().contains("null"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should enforce unique constraints")
        void validateUniqueConstraints(UniAsserter asserter) {
            IssueComment comment1 = TestEntityCreators.createIssueComment(testUser, testRepo, "1");
            IssueComment comment2 = TestEntityCreators.createIssueComment(testUser, testRepo, "2");
            comment2.setGithubId(comment1.getGithubId());
            comment2.setGithubUrl(comment1.getGithubUrl());

            asserter.execute(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comment1)));

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> issueCommentRepository.persist(comment2)),
                    throwable -> assertTrue(
                            throwable.getMessage().toLowerCase().contains("constraint"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }
}