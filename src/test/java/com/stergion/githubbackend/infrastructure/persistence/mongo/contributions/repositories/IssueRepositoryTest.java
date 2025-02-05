package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.Issue;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Github;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Label;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.NameWithOwner;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.UserWithLogin;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueState;
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
@DisplayName("IssueRepository Tests")
class IssueRepositoryTest {

    @Inject
    IssueRepository issueRepository;

    private static final ObjectId TEST_USER_ID = new ObjectId();
    private static final ObjectId TEST_REPO_ID = new ObjectId();
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private Issue testIssue;

    @BeforeEach
    void setUp() {
        issueRepository.deleteAll().await().indefinitely();
        testIssue = createTestIssue(null, TEST_USER_ID, TEST_REPO_ID);
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("Should persist and retrieve issue by ID")
        void persistAndFindById() {
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            assertNotNull(testIssue.id(), "Issue ID should be generated");

            Issue found = issueRepository.findById(testIssue.id())
                                         .await().atMost(TIMEOUT);

            assertNotNull(found, "Found issue should not be null");
            assertEquals(testIssue.id(), found.id());
            assertEquals(testIssue.userId(), found.userId());
            assertEquals(testIssue.repositoryId(), found.repositoryId());
            assertEquals(testIssue.github().id, found.github().id);
            assertEquals(testIssue.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should delete issue by user ID")
        void deleteByUserId() {
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            issueRepository.delete(TEST_USER_ID).await().atMost(TIMEOUT);

            Issue found = issueRepository.findById(testIssue.id())
                                         .await().atMost(TIMEOUT);
            assertNull(found, "Issue should be deleted");
        }
    }

    @Nested
    @DisplayName("Search Operations")
    class SearchOperations {

        @Test
        @DisplayName("Should find issue by GitHub ID")
        void findByGitHubId() {
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            Issue found = issueRepository.findByGitHubId(testIssue.github().id)
                                         .await().atMost(TIMEOUT);

            assertNotNull(found, "Found issue should not be null");
            assertEquals(testIssue.id(), found.id());
            assertEquals(testIssue.userId(), found.userId());
            assertEquals(testIssue.repositoryId(), found.repositoryId());
            assertEquals(testIssue.github().id, found.github().id);
            assertEquals(testIssue.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should find issues by user ID")
        void findByUserId() {
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            List<Issue> found = issueRepository.findByUserId(TEST_USER_ID)
                                               .collect().asList()
                                               .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one issue");
            assertNotNull(found, "Found issues should not be null");
            assertEquals(testIssue.id(), found.getFirst().id());
            assertEquals(testIssue.userId(), found.getFirst().userId());
            assertEquals(testIssue.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testIssue.github().id, found.getFirst().github().id);
            assertEquals(testIssue.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find issues by repository ID")
        void findByRepoId() {
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            List<Issue> found = issueRepository.findByRepoId(TEST_REPO_ID)
                                               .collect().asList()
                                               .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one issue");
            assertNotNull(found, "Found issues should not be null");
            assertEquals(testIssue.id(), found.getFirst().id());
            assertEquals(testIssue.userId(), found.getFirst().userId());
            assertEquals(testIssue.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testIssue.github().id, found.getFirst().github().id);
            assertEquals(testIssue.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find issues by user ID and repository ID")
        void findByUserAndRepoId() {
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            List<Issue> found = issueRepository.findByUserAndRepoId(TEST_USER_ID, TEST_REPO_ID)
                                               .collect().asList()
                                               .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one issue");
            assertNotNull(found, "Found issues should not be null");
            assertEquals(testIssue.id(), found.getFirst().id());
            assertEquals(testIssue.userId(), found.getFirst().userId());
            assertEquals(testIssue.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testIssue.github().id, found.getFirst().github().id);
            assertEquals(testIssue.createdAt(), found.getFirst().createdAt());
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

            // Create multiple issues for testing
            persistTestIssues(userId1, userId2, repoId1, repoId2);

            List<ObjectId> repositoryIds = issueRepository.getRepositoryIds(userId1)
                                                          .await().atMost(TIMEOUT);

            assertEquals(2, repositoryIds.size(), "Should find two distinct repositories");
            assertTrue(repositoryIds.contains(repoId1), "Should contain first repo ID");
            assertTrue(repositoryIds.contains(repoId2), "Should contain second repo ID");
        }

        @Test
        @DisplayName("Should return empty list when user has no issues")
        void getRepositoryIdsForUserWithNoIssues() {
            List<ObjectId> repositoryIds = issueRepository.getRepositoryIds(new ObjectId())
                                                          .await().atMost(TIMEOUT);

            assertTrue(repositoryIds.isEmpty(),
                    "Should return empty list for user with no issues");
        }
    }

    @Nested
    @DisplayName("Issue State Operations")
    class IssueStateOperations {

        @Test
        @DisplayName("Should find open issues")
        void findOpenIssues() {
            testIssue.state = IssueState.OPEN;
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            List<Issue> openIssues = issueRepository.findByUserIdAndState(TEST_USER_ID,
                                                            IssueState.OPEN)
                                                    .collect().asList()
                                                    .await().atMost(TIMEOUT);

            assertFalse(openIssues.isEmpty(), "Should find open issues");
            assertEquals(IssueState.OPEN, openIssues.getFirst().state());
        }

        @Test
        @DisplayName("Should find closed issues")
        void findClosedIssues() {
            testIssue.state = IssueState.CLOSED;
            testIssue.closedAt = LocalDate.now();
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            List<Issue> closedIssues = issueRepository.findByUserIdAndState(TEST_USER_ID,
                                                              IssueState.CLOSED)
                                                      .collect().asList()
                                                      .await().atMost(TIMEOUT);

            assertFalse(closedIssues.isEmpty(), "Should find closed issues");
            assertEquals(IssueState.CLOSED, closedIssues.getFirst().state());
            assertNotNull(closedIssues.getFirst().closedAt(),
                    "Closed issue should have closedAt date");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null values in optional fields")
        void handleNullOptionalFields() {
            Issue issueWithNulls = createTestIssue(null, TEST_USER_ID, TEST_REPO_ID);
            issueWithNulls.closedAt = null;
            issueWithNulls.updatedAt = null;
            issueWithNulls.labels = null;
            issueWithNulls.closer = null;

            issueRepository.persist(issueWithNulls).await().atMost(TIMEOUT);

            Issue found = issueRepository.findById(issueWithNulls.id())
                                         .await().atMost(TIMEOUT);

            assertNotNull(found, "Found issue should not be null");
            assertEquals(issueWithNulls.id(), found.id());
            assertEquals(issueWithNulls.userId(), found.userId());
            assertEquals(issueWithNulls.repositoryId(), found.repositoryId());
            assertEquals(issueWithNulls.github().id, found.github().id);
            assertEquals(issueWithNulls.createdAt(), found.createdAt());
            assertNull(found.closedAt(), "ClosedAt should be null");
            assertNull(found.updatedAt(), "UpdatedAt should be null");
            assertNull(found.labels(), "Labels should be null");
            assertNull(found.closer(), "Closer should be null");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100})
        @DisplayName("Should handle different issue counts")
        void handleDifferentIssueCounts(int issueCount) {
            for (int i = 0; i < issueCount; i++) {
                issueRepository.persist(createTestIssue(null, TEST_USER_ID, TEST_REPO_ID))
                               .await().atMost(TIMEOUT);
            }

            List<Issue> found = issueRepository.findByUserId(TEST_USER_ID)
                                               .collect().asList()
                                               .await().atMost(TIMEOUT);

            assertEquals(issueCount, found.size(),
                    "Should find correct number of issues");
        }
    }

    @Nested
    @DisplayName("Issue State Transition Operations")
    class IssueStateTransitionOperations {
        @Test
        @DisplayName("Should handle complete issue lifecycle")
        void handleIssueLifecycle() {
            // Create new issue
            testIssue.state = IssueState.OPEN;
            testIssue.closedAt = null;
            issueRepository.persist(testIssue).await().atMost(TIMEOUT);

            Issue newIssue = issueRepository.findById(testIssue.id())
                                            .await().atMost(TIMEOUT);
            assertEquals(IssueState.OPEN, newIssue.state(), "New issue should be open");
            assertNull(newIssue.closedAt(), "New issue should not have closed date");

            // Close issue
            testIssue.state = IssueState.CLOSED;
            testIssue.closedAt = LocalDate.now();
            testIssue.closer = "test-closer";
            issueRepository.update(testIssue).await().atMost(TIMEOUT);

            Issue closedIssue = issueRepository.findById(testIssue.id())
                                               .await().atMost(TIMEOUT);
            assertEquals(IssueState.CLOSED, closedIssue.state(), "Issue should be closed");
            assertNotNull(closedIssue.closedAt(), "Closed issue should have closed date");
            assertEquals("test-closer", closedIssue.closer(), "Should record who closed the issue");

            // Reopen issue
            testIssue.state = IssueState.OPEN;
            testIssue.closedAt = null;
            testIssue.closer = null;
            issueRepository.update(testIssue).await().atMost(TIMEOUT);

            Issue reopenedIssue = issueRepository.findById(testIssue.id())
                                                 .await().atMost(TIMEOUT);
            assertEquals(IssueState.OPEN, reopenedIssue.state(), "Issue should be reopened");
            assertNull(reopenedIssue.closedAt(), "Reopened issue should not have closed date");
            assertNull(reopenedIssue.closer(), "Reopened issue should not have closer");
        }
    }

    // Helper methods
    private void persistTestIssues(ObjectId userId1, ObjectId userId2, ObjectId repoId1,
                                   ObjectId repoId2) {
        issueRepository.persist(createTestIssue(null, userId1, repoId1)).await().atMost(TIMEOUT);
        issueRepository.persist(createTestIssue(null, userId1, repoId2)).await().atMost(TIMEOUT);
        issueRepository.persist(createTestIssue(null, userId1, repoId2)).await().atMost(TIMEOUT);
        issueRepository.persist(createTestIssue(null, userId2, repoId1)).await().atMost(TIMEOUT);
        issueRepository.persist(createTestIssue(null, userId2, repoId2)).await().atMost(TIMEOUT);
    }

    private Issue createTestIssue(ObjectId id, ObjectId userId, ObjectId repoId) {
        var github = new Github();
        github.id = "test-github-id";
        var userWithLogin = new UserWithLogin("test-user");
        var nameWithOwner = new NameWithOwner("test-repo", "test-user");

        var issue = new Issue();
        issue.id = id;
        issue.userId = userId;
        issue.repositoryId = repoId;
        issue.user = userWithLogin;
        issue.repository = nameWithOwner;
        issue.github = github;
        issue.createdAt = LocalDate.now();
        issue.closedAt = null;
        issue.updatedAt = LocalDate.now();
        issue.state = IssueState.OPEN;
        issue.title = "Test Issue";
        issue.body = "Test issue body";
        issue.reactionsCount = 0;
        issue.labels = List.of(new Label("bug", "bug label description"),
                new Label("enhancement", "enhancement label description"));
        issue.closer = null;

        return issue;
    }
}