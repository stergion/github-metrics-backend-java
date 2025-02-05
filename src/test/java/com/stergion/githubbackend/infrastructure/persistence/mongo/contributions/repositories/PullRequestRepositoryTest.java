package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.*;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.PullRequestState;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("PullRequestRepository Tests")
class PullRequestRepositoryTest {

    @Inject
    PullRequestRepository pullRequestRepository;

    private static final ObjectId TEST_USER_ID = new ObjectId();
    private static final ObjectId TEST_REPO_ID = new ObjectId();
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private PullRequest testPullRequest;

    @BeforeEach
    void setUp() {
        pullRequestRepository.deleteAll().await().indefinitely();
        testPullRequest = createTestPullRequest(null, TEST_USER_ID, TEST_REPO_ID);
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("Should persist and retrieve pull request by ID")
        void persistAndFindById() {
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            assertNotNull(testPullRequest.id(), "Pull Request ID should be generated");

            PullRequest found = pullRequestRepository.findById(testPullRequest.id())
                                                     .await()
                                                     .atMost(TIMEOUT);
            assertNotNull(found, "Found pull request should not be null");
            assertEquals(testPullRequest.id(), found.id());
            assertEquals(testPullRequest.userId(), found.userId());
            assertEquals(testPullRequest.repositoryId(), found.repositoryId());
            assertEquals(testPullRequest.github().id, found.github().id);
            assertEquals(testPullRequest.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should delete pull request by user ID")
        void deleteByUserId() {
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            pullRequestRepository.delete(TEST_USER_ID).await().atMost(TIMEOUT);

            assertNull(pullRequestRepository.findById(testPullRequest.id()).await().atMost(TIMEOUT),
                    "Pull request should be deleted");
        }
    }

    @Nested
    @DisplayName("Search Operations")
    class SearchOperations {

        @Test
        @DisplayName("Should find pull request by GitHub ID")
        void findByGitHubId() {
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            PullRequest found = pullRequestRepository.findByGitHubId(testPullRequest.github().id)
                                                     .await()
                                                     .atMost(TIMEOUT);
            assertNotNull(found, "Found pull request should not be null");
            assertEquals(testPullRequest.id(), found.id());
            assertEquals(testPullRequest.userId(), found.userId());
            assertEquals(testPullRequest.repositoryId(), found.repositoryId());
            assertEquals(testPullRequest.github().id, found.github().id);
            assertEquals(testPullRequest.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should find pull requests by user ID")
        void findByUserId() {
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            List<PullRequest> found = pullRequestRepository.findByUserId(TEST_USER_ID)
                                                           .collect().asList()
                                                           .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one pull request");
            assertNotNull(found, "Found pull requests should not be null");
            assertEquals(testPullRequest.id(), found.getFirst().id());
            assertEquals(testPullRequest.userId(), found.getFirst().userId());
            assertEquals(testPullRequest.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testPullRequest.github().id, found.getFirst().github().id);
            assertEquals(testPullRequest.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find pull requests by repository ID")
        void findByRepoId() {
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            List<PullRequest> found = pullRequestRepository.findByRepoId(TEST_REPO_ID)
                                                           .collect().asList()
                                                           .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one pull request");
            assertNotNull(found, "Found pull requests should not be null");
            assertEquals(testPullRequest.id(), found.getFirst().id());
            assertEquals(testPullRequest.userId(), found.getFirst().userId());
            assertEquals(testPullRequest.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testPullRequest.github().id, found.getFirst().github().id);
            assertEquals(testPullRequest.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find pull requests by user ID and repository ID")
        void findByUserAndRepoId() {
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            List<PullRequest> found = pullRequestRepository.findByUserAndRepoId(TEST_USER_ID,
                                                                   TEST_REPO_ID)
                                                           .collect().asList()
                                                           .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one pull request");
            assertNotNull(found, "Found pull requests should not be null");
            assertEquals(testPullRequest.id(), found.getFirst().id());
            assertEquals(testPullRequest.userId(), found.getFirst().userId());
            assertEquals(testPullRequest.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testPullRequest.github().id, found.getFirst().github().id);
            assertEquals(testPullRequest.createdAt(), found.getFirst().createdAt());
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

            // Create multiple pull requests for testing
            persistTestPullRequests(userId1, userId2, repoId1, repoId2);

            var repositoryIds = pullRequestRepository.getRepositoryIds(userId1)
                                                     .await()
                                                     .atMost(TIMEOUT);

            assertEquals(2, repositoryIds.size(), "Should find two distinct repositories");
            assertTrue(repositoryIds.contains(repoId1), "Should contain first repo ID");
            assertTrue(repositoryIds.contains(repoId2), "Should contain second repo ID");
        }

        @Test
        @DisplayName("Should return empty list when user has no pull requests")
        void getRepositoryIdsForUserWithNoPullRequests() {
            var repositoryIds = pullRequestRepository.getRepositoryIds(new ObjectId())
                                                     .await()
                                                     .atMost(TIMEOUT);

            assertTrue(repositoryIds.isEmpty(),
                    "Should return empty list for user with no pull requests");
        }
    }

    @Nested
    @DisplayName("Pull Request State Operations")
    class PullRequestStateOperations {
        @Test
        @DisplayName("Should find pull requests by user ID and state")
        void findByUserIdAndState() {
            // Create PRs in different states
            ObjectId userId = new ObjectId();

            var openPR = createTestPullRequest(null, userId, TEST_REPO_ID);
            openPR.state = PullRequestState.OPEN;

            var closedPR = createTestPullRequest(null, userId, TEST_REPO_ID);
            closedPR.state = PullRequestState.CLOSED;
            closedPR.closedAt = LocalDate.now();

            var mergedPR = createTestPullRequest(null, userId, TEST_REPO_ID);
            mergedPR.state = PullRequestState.MERGED;
            mergedPR.mergedAt = LocalDate.now();

            pullRequestRepository.persist(openPR).await().atMost(TIMEOUT);
            pullRequestRepository.persist(closedPR).await().atMost(TIMEOUT);
            pullRequestRepository.persist(mergedPR).await().atMost(TIMEOUT);

            // Test finding each state
            var openPRs = pullRequestRepository.findByUserIdAndState(userId, PullRequestState.OPEN)
                                               .collect().asList()
                                               .await().atMost(TIMEOUT);
            assertEquals(1, openPRs.size(), "Should find one open PR");
            assertEquals(PullRequestState.OPEN, openPRs.getFirst().state(), "PR should be open");

            var closedPRs = pullRequestRepository.findByUserIdAndState(userId,
                                                         PullRequestState.CLOSED)
                                                 .collect().asList()
                                                 .await().atMost(TIMEOUT);
            assertEquals(1, closedPRs.size(), "Should find one closed PR");
            assertEquals(PullRequestState.CLOSED, closedPRs.getFirst().state(),
                    "PR should be closed");
            assertNotNull(closedPRs.getFirst().closedAt(), "Closed PR should have closedAt date");

            var mergedPRs = pullRequestRepository.findByUserIdAndState(userId,
                                                         PullRequestState.MERGED)
                                                 .collect().asList()
                                                 .await().atMost(TIMEOUT);
            assertEquals(1, mergedPRs.size(), "Should find one merged PR");
            assertEquals(PullRequestState.MERGED, mergedPRs.getFirst().state(),
                    "PR should be merged");
            assertNotNull(mergedPRs.getFirst().mergedAt(), "Merged PR should have mergedAt date");
        }

        @Test
        @DisplayName("Should handle merged pull requests")
        void handleMergedPullRequests() {
            testPullRequest.state = PullRequestState.MERGED;
            testPullRequest.mergedAt = LocalDate.now();
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            PullRequest found = pullRequestRepository.findById(testPullRequest.id())
                                                     .await()
                                                     .atMost(TIMEOUT);

            assertNotNull(found, "Found pull request should not be null");
            assertEquals(PullRequestState.MERGED, found.state());
            assertNotNull(found.mergedAt(), "Merged pull request should have mergedAt date");
        }

        @Test
        @DisplayName("Should handle closed pull requests")
        void handleClosedPullRequests() {
            testPullRequest.state = PullRequestState.CLOSED;
            testPullRequest.closedAt = LocalDate.now();
            pullRequestRepository.persist(testPullRequest).await().atMost(TIMEOUT);

            PullRequest found = pullRequestRepository.findById(testPullRequest.id())
                                                     .await()
                                                     .atMost(TIMEOUT);

            assertNotNull(found, "Found pull request should not be null");
            assertEquals(PullRequestState.CLOSED, found.state());
            assertNotNull(found.closedAt(), "Closed pull request should have closedAt date");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null values in optional fields")
        void handleNullOptionalFields() {
            PullRequest prWithNulls = createTestPullRequest(null, TEST_USER_ID, TEST_REPO_ID);
            prWithNulls.mergedAt = null;
            prWithNulls.closedAt = null;
            prWithNulls.updatedAt = null;
            prWithNulls.labels = null;
            prWithNulls.commits = null;
            prWithNulls.closingIssuesReferences = null;

            pullRequestRepository.persist(prWithNulls).await().atMost(TIMEOUT);

            PullRequest found = pullRequestRepository.findById(prWithNulls.id())
                                                     .await()
                                                     .atMost(TIMEOUT);

            assertNotNull(found, "Found pull request should not be null");
            assertEquals(prWithNulls.id(), found.id());
            assertEquals(prWithNulls.userId(), found.userId());
            assertEquals(prWithNulls.repositoryId(), found.repositoryId());
            assertEquals(prWithNulls.github().id, found.github().id);
            assertEquals(prWithNulls.createdAt(), found.createdAt());
            assertNull(found.mergedAt(), "MergedAt should be null");
            assertNull(found.closedAt(), "ClosedAt should be null");
            assertNull(found.updatedAt(), "UpdatedAt should be null");
            assertNull(found.labels(), "Labels should be null");
            assertNull(found.commits(), "Commits should be null");
            assertNull(found.closingIssuesReferences(), "ClosingIssuesReferences should be null");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100})
        @DisplayName("Should handle different pull request counts")
        void handleDifferentPullRequestCounts(int prCount) {
            for (int i = 0; i < prCount; i++) {
                pullRequestRepository.persist(
                                             createTestPullRequest(null, TEST_USER_ID,
                                                     TEST_REPO_ID))
                                     .await()
                                     .atMost(TIMEOUT);
            }

            List<PullRequest> found = pullRequestRepository.findByUserId(TEST_USER_ID)
                                                           .collect().asList()
                                                           .await().atMost(TIMEOUT);
            assertEquals(prCount, found.size(),
                    "Should find correct number of pull requests");
        }
    }

    // Helper methods
    private void persistTestPullRequests(ObjectId userId1, ObjectId userId2, ObjectId repoId1,
                                         ObjectId repoId2) {
        pullRequestRepository.persist(createTestPullRequest(null, userId1, repoId1)).await().atMost(TIMEOUT);
        pullRequestRepository.persist(createTestPullRequest(null, userId1, repoId2)).await().atMost(TIMEOUT);
        pullRequestRepository.persist(createTestPullRequest(null, userId1, repoId2)).await().atMost(TIMEOUT);
        pullRequestRepository.persist(createTestPullRequest(null, userId2, repoId1)).await().atMost(TIMEOUT);
        pullRequestRepository.persist(createTestPullRequest(null, userId2, repoId2)).await().atMost(TIMEOUT);
    }

    private PullRequest createTestPullRequest(ObjectId id, ObjectId userId, ObjectId repoId) {
        var github = new Github();
        github.id = "test-github-id";
        var userWithLogin = new UserWithLogin("test-user");
        var nameWithOwner = new NameWithOwner("test-repo", "test-user");

        var pullRequest = new PullRequest();
        pullRequest.id = id;
        pullRequest.userId = userId;
        pullRequest.repositoryId = repoId;
        pullRequest.user = userWithLogin;
        pullRequest.repository = nameWithOwner;
        pullRequest.github = github;
        pullRequest.createdAt = LocalDate.now();
        pullRequest.mergedAt = null;
        pullRequest.closedAt = null;
        pullRequest.updatedAt = LocalDate.now();
        pullRequest.state = PullRequestState.OPEN;
        pullRequest.title = "Test Pull Request";
        pullRequest.body = "Test pull request body";
        pullRequest.reactionsCount = 0;
        pullRequest.labels = List.of(new Label("bug", "bug label description"),
                new Label("enhancement", "enhancement label description"));
        pullRequest.commits = List.of(new PullRequestCommit());
        pullRequest.commitsCount = 1;
        pullRequest.commentsCount = 0;
        pullRequest.closingIssuesReferences = List.of();
        pullRequest.closingIssuesReferencesCount = 0;

        return pullRequest;
    }
}