package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.Commit;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.UserWithLogin;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("CommitRepository Tests")
class CommitRepositoryTest {

    @Inject
    CommitRepository commitRepository;

    private static final ObjectId TEST_USER_ID = new ObjectId();
    private static final ObjectId TEST_REPO_ID = new ObjectId();
    private Commit testCommit;

    @BeforeEach
    void setUp() {
        commitRepository.deleteAll().await().indefinitely();
        testCommit = createTestCommit(null, TEST_USER_ID, TEST_REPO_ID, Optional.empty());
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("Should persist and retrieve commit by ID")
        void persistAndFindById() {
            commitRepository.persist(testCommit).await().indefinitely();

            assertNotNull(testCommit.id(), "Commit ID should be generated");

            Commit found = commitRepository.findById(testCommit.id()).await().indefinitely();
            assertNotNull(found, "Found commit should not be null");
            assertEquals(testCommit.id(), found.id());
            assertEquals(testCommit.userId(), found.userId());
            assertEquals(testCommit.repositoryId(), found.repositoryId());
            assertEquals(testCommit.github().id, found.github().id);
            assertEquals(testCommit.committedDate(), found.committedDate());
        }

        @Test
        @DisplayName("Should delete commit by user ID")
        void deleteByUserId() {
            commitRepository.persist(testCommit).await().indefinitely();

            commitRepository.delete(TEST_USER_ID).await().indefinitely();

            assertNull(commitRepository.findById(testCommit.id()).await().indefinitely(),
                    "Commit should be deleted");
        }
    }

    @Nested
    @DisplayName("Search Operations")
    class SearchOperations {

        @Test
        @DisplayName("Should find commit by GitHub ID")
        void findByGitHubId() {
            commitRepository.persist(testCommit).await().indefinitely();

            Commit found = commitRepository.findByGitHubId(testCommit.github().id)
                                           .await()
                                           .indefinitely();
            assertNotNull(found, "Found commit should not be null");
            assertEquals(testCommit.id(), found.id());
            assertEquals(testCommit.userId(), found.userId());
            assertEquals(testCommit.repositoryId(), found.repositoryId());
            assertEquals(testCommit.github().id, found.github().id);
            assertEquals(testCommit.committedDate(), found.committedDate());
        }

        @Test
        @DisplayName("Should find commits by user ID")
        void findByUserId() {
            commitRepository.persist(testCommit).await().indefinitely();

            List<Commit> found = commitRepository.findByUserId(TEST_USER_ID)
                                                 .subscribe().asIterable()
                                                 .stream().toList();

            assertFalse(found.isEmpty(), "Should find at least one commit");
            assertNotNull(found, "Found commit should not be null");
            assertEquals(testCommit.id(), found.getFirst().id());
            assertEquals(testCommit.userId(), found.getFirst().userId());
            assertEquals(testCommit.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testCommit.github().id, found.getFirst().github().id);
            assertEquals(testCommit.committedDate(), found.getFirst().committedDate());
        }

        @Test
        @DisplayName("Should find commits by repository ID")
        void findByRepoId() {
            commitRepository.persist(testCommit).await().indefinitely();

            List<Commit> found = commitRepository.findByRepoId(TEST_REPO_ID)
                                                 .subscribe().asIterable()
                                                 .stream().toList();

            assertFalse(found.isEmpty(), "Should find at least one commit");
            assertNotNull(found, "Found commit should not be null");
            assertEquals(testCommit.id(), found.getFirst().id());
            assertEquals(testCommit.userId(), found.getFirst().userId());
            assertEquals(testCommit.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testCommit.github().id, found.getFirst().github().id);
            assertEquals(testCommit.committedDate(), found.getFirst().committedDate());
        }

        @Test
        @DisplayName("Should find commits by user ID and repository ID")
        void findByUserAndRepoId() {
            commitRepository.persist(testCommit).await().indefinitely();

            List<Commit> found = commitRepository.findByUserAndRepoId(TEST_USER_ID, TEST_REPO_ID)
                                                 .subscribe().asIterable()
                                                 .stream().toList();

            assertFalse(found.isEmpty(), "Should find at least one commit");
            assertNotNull(found, "Found commit should not be null");
            assertEquals(testCommit.id(), found.getFirst().id());
            assertEquals(testCommit.userId(), found.getFirst().userId());
            assertEquals(testCommit.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testCommit.github().id, found.getFirst().github().id);
            assertEquals(testCommit.committedDate(), found.getFirst().committedDate());
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

            // Create multiple commits for testing
            persistTestCommits(userId1, userId2, repoId1, repoId2);

            List<ObjectId> repositoryIds = commitRepository.getRepositoryIds(userId1)
                                                           .await().indefinitely();

            assertEquals(2, repositoryIds.size(), "Should find two distinct repositories");
            assertTrue(repositoryIds.contains(repoId1), "Should contain first repo ID");
            assertTrue(repositoryIds.contains(repoId2), "Should contain second repo ID");
        }

        @Test
        @DisplayName("Should return empty list when user has no commits")
        void getRepositoryIdsForUserWithNoCommits() {
            List<ObjectId> repositoryIds = commitRepository.getRepositoryIds(new ObjectId())
                                                           .await().indefinitely();

            assertTrue(repositoryIds.isEmpty(),
                    "Should return empty list for user with no commits");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null values in optional fields")
        void handleNullOptionalFields() {
            Commit commitWithNulls = createTestCommit(null, TEST_USER_ID, TEST_REPO_ID, Optional.of("1"));
            commitRepository.persist(commitWithNulls).await().indefinitely();

            Commit found = commitRepository.findById(commitWithNulls.id()).await().indefinitely();

            assertNotNull(found, "Found commit should not be null");
            assertEquals(commitWithNulls.id(), found.id());
            assertEquals(commitWithNulls.userId(), found.userId());
            assertEquals(commitWithNulls.repositoryId(), found.repositoryId());
            assertEquals(commitWithNulls.github().id, found.github().id);
            assertEquals(commitWithNulls.committedDate(), found.committedDate());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100})
        @DisplayName("Should handle different commit counts")
        void handleDifferentCommitCounts(int commitCount) {
            for (int i = 0; i < commitCount; i++) {
                commitRepository.persist(createTestCommit(null, TEST_USER_ID, TEST_REPO_ID,
                        Optional.of(String.valueOf(i)))).await().indefinitely();
            }

            List<Commit> found = commitRepository.findByUserId(TEST_USER_ID)
                                                 .subscribe().asIterable()
                                                 .stream().toList();
            assertEquals(commitCount, found.size(),
                    "Should find correct number of commits");
        }
    }

    // Helper methods
    private void persistTestCommits(ObjectId userId1, ObjectId userId2, ObjectId repoId1,
                                    ObjectId repoId2) {
        commitRepository.persist(createTestCommit(null, userId1, repoId1, Optional.of("1")))
                        .await()
                        .indefinitely();
        commitRepository.persist(createTestCommit(null, userId1, repoId2, Optional.of("2")))
                        .await()
                        .indefinitely();
        commitRepository.persist(createTestCommit(null, userId1, repoId2, Optional.of("3")))
                        .await()
                        .indefinitely();
        commitRepository.persist(createTestCommit(null, userId2, repoId1, Optional.of("4")))
                        .await()
                        .indefinitely();
        commitRepository.persist(createTestCommit(null, userId2, repoId2, Optional.of("5")))
                        .await()
                        .indefinitely();
    }

    // createTestCommit method remains unchanged
    private Commit createTestCommit(ObjectId id, ObjectId userId, ObjectId repoId,
                                    Optional<String> suffix) {
        var github = new Github();
        github.id = "test-github-" + suffix.orElse("id");
        github.url = URI.create("www.github.com/test-github-" + suffix.orElse("url"));
        var userWithLogin = new UserWithLogin("test-user");
        var nameWithOwner = new NameWithOwner("test-repo", "test-user");

        var commit = new Commit();
        commit.id = id;
        commit.userId = userId;
        commit.repositoryId = repoId;
        commit.user = userWithLogin;
        commit.repository = nameWithOwner;
        commit.github = github;
        commit.committedDate = LocalDate.now();
        commit.pushedDate = null;
        commit.additions = 0;
        commit.deletions = 0;
        commit.comments = null;
        commit.commentsCount = 0;
        commit.associatedPullRequest = null;
        commit.associatedPullRequestsCount = 0;
        commit.files = null;
        commit.filesCount = 0;

        return commit;
    }
}