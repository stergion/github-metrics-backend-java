package com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories;

import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.IssueCommentEntity;
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
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("IssueCommentRepository Tests")
class IssueCommentRepositoryTest {

    @Inject
    IssueCommentRepository issueCommentRepository;

    private static final ObjectId TEST_USER_ID = new ObjectId();
    private static final ObjectId TEST_REPO_ID = new ObjectId();
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private IssueCommentEntity testComment;

    @BeforeEach
    void setUp() {
        issueCommentRepository.deleteAll().await().atMost(TIMEOUT);
        testComment = createTestComment(null, TEST_USER_ID, TEST_REPO_ID, Optional.empty());
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("Should persist and retrieve comment by ID")
        void persistAndFindById() {
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            assertNotNull(testComment.id(), "Comment ID should be generated");

            IssueCommentEntity found = issueCommentRepository.findById(testComment.id())
                                                             .await().atMost(TIMEOUT);

            assertNotNull(found, "Found comment should not be null");
            assertEquals(testComment.id(), found.id());
            assertEquals(testComment.userId(), found.userId());
            assertEquals(testComment.repositoryId(), found.repositoryId());
            assertEquals(testComment.github().id, found.github().id);
            assertEquals(testComment.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should delete comment by user ID")
        void deleteByUserId() {
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            issueCommentRepository.deleteByUserId(TEST_USER_ID).await().atMost(TIMEOUT);

            IssueCommentEntity found = issueCommentRepository.findById(testComment.id())
                                                             .await().atMost(TIMEOUT);
            assertNull(found, "Comment should be deleted");
        }
    }

    @Nested
    @DisplayName("Search Operations")
    class SearchOperations {

        @Test
        @DisplayName("Should find comment by GitHub ID")
        void findByGitHubId() {
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            IssueCommentEntity found = issueCommentRepository.findByGitHubId(testComment.github().id)
                                                             .await().atMost(TIMEOUT);

            assertNotNull(found, "Found comment should not be null");
            assertEquals(testComment.id(), found.id());
            assertEquals(testComment.userId(), found.userId());
            assertEquals(testComment.repositoryId(), found.repositoryId());
            assertEquals(testComment.github().id, found.github().id);
            assertEquals(testComment.createdAt(), found.createdAt());
        }

        @Test
        @DisplayName("Should find comments by user ID")
        void findByUserId() {
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            List<IssueCommentEntity> found = issueCommentRepository.findByUserId(TEST_USER_ID)
                                                                   .collect().asList()
                                                                   .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one comment");
            assertNotNull(found, "Found comments should not be null");
            assertEquals(testComment.id(), found.getFirst().id());
            assertEquals(testComment.userId(), found.getFirst().userId());
            assertEquals(testComment.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testComment.github().id, found.getFirst().github().id);
            assertEquals(testComment.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find comments by repository ID")
        void findByRepoId() {
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            List<IssueCommentEntity> found = issueCommentRepository.findByRepoId(TEST_REPO_ID)
                                                                   .collect().asList()
                                                                   .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one comment");
            assertNotNull(found, "Found comments should not be null");
            assertEquals(testComment.id(), found.getFirst().id());
            assertEquals(testComment.userId(), found.getFirst().userId());
            assertEquals(testComment.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testComment.github().id, found.getFirst().github().id);
            assertEquals(testComment.createdAt(), found.getFirst().createdAt());
        }

        @Test
        @DisplayName("Should find comments by user ID and repository ID")
        void findByUserAndRepoId() {
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            List<IssueCommentEntity> found = issueCommentRepository.findByUserAndRepoId(TEST_USER_ID,
                                                                     TEST_REPO_ID)
                                                                   .collect().asList()
                                                                   .await().atMost(TIMEOUT);

            assertFalse(found.isEmpty(), "Should find at least one comment");
            assertNotNull(found, "Found comments should not be null");
            assertEquals(testComment.id(), found.getFirst().id());
            assertEquals(testComment.userId(), found.getFirst().userId());
            assertEquals(testComment.repositoryId(), found.getFirst().repositoryId());
            assertEquals(testComment.github().id, found.getFirst().github().id);
            assertEquals(testComment.createdAt(), found.getFirst().createdAt());
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

            // Create multiple comments for testing
            persistTestComments(userId1, userId2, repoId1, repoId2);

            List<ObjectId> repositoryIds = issueCommentRepository.getRepositoryIds(userId1)
                                                                 .await().atMost(TIMEOUT);

            assertEquals(2, repositoryIds.size(), "Should find two distinct repositories");
            assertTrue(repositoryIds.contains(repoId1), "Should contain first repo ID");
            assertTrue(repositoryIds.contains(repoId2), "Should contain second repo ID");
        }

        @Test
        @DisplayName("Should return empty list when user has no comments")
        void getRepositoryIdsForUserWithNoComments() {
            List<ObjectId> repositoryIds = issueCommentRepository.getRepositoryIds(new ObjectId())
                                                                 .await().atMost(TIMEOUT);

            assertTrue(repositoryIds.isEmpty(),
                    "Should return empty list for user with no comments");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null values in optional fields")
        void handleNullOptionalFields() {
            IssueCommentEntity commentWithNulls = createTestComment(null, TEST_USER_ID, TEST_REPO_ID, Optional.of("1"));
            issueCommentRepository.persist(commentWithNulls).await().atMost(TIMEOUT);

            IssueCommentEntity found = issueCommentRepository.findById(commentWithNulls.id())
                                                             .await().atMost(TIMEOUT);

            assertNotNull(found, "Found comment should not be null");
            assertEquals(commentWithNulls.id(), found.id());
            assertEquals(commentWithNulls.userId(), found.userId());
            assertEquals(commentWithNulls.repositoryId(), found.repositoryId());
            assertEquals(commentWithNulls.github().id, found.github().id);
            assertEquals(commentWithNulls.createdAt(), found.createdAt());
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 100})
        @DisplayName("Should handle different comment counts")
        void handleDifferentCommentCounts(int commentCount) {
            for (int i = 0; i < commentCount; i++) {
                issueCommentRepository.persist(createTestComment(null, TEST_USER_ID, TEST_REPO_ID,
                                              Optional.of(String.valueOf(i))))
                                      .await().atMost(TIMEOUT);
            }

            List<IssueCommentEntity> found = issueCommentRepository.findByUserId(TEST_USER_ID)
                                                                   .collect().asList()
                                                                   .await().atMost(TIMEOUT);

            assertEquals(commentCount, found.size(),
                    "Should find correct number of comments");
        }
    }

    @Nested
    @DisplayName("Comment Lifecycle Operations")
    class CommentLifecycleOperations {
        @Test
        @DisplayName("Should handle comment publication lifecycle")
        void handleCommentPublication() {
            testComment.publishedAt = null;  // Start unpublished
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            IssueCommentEntity unpublished = issueCommentRepository.findById(testComment.id())
                                                                   .await().atMost(TIMEOUT);
            assertNull(unpublished.publishedAt(), "New comment should be unpublished");

            // Simulate publication
            testComment.publishedAt = LocalDate.now();
            issueCommentRepository.update(testComment).await().atMost(TIMEOUT);

            IssueCommentEntity published = issueCommentRepository.findById(testComment.id())
                                                                 .await().atMost(TIMEOUT);
            assertNotNull(published.publishedAt(), "Comment should be published");
        }

        @Test
        @DisplayName("Should handle comment editing lifecycle")
        void handleCommentEditing() {
            testComment.lastEditedAt = null;  // Start unedited
            testComment.body = "Original content";
            issueCommentRepository.persist(testComment).await().atMost(TIMEOUT);

            IssueCommentEntity original = issueCommentRepository.findById(testComment.id())
                                                                .await().atMost(TIMEOUT);
            assertNull(original.lastEditedAt(), "New comment should not be edited");
            assertEquals("Original content", original.body(), "Should have original content");

            // Simulate edit
            testComment.lastEditedAt = LocalDate.now();
            testComment.body = "Updated content";
            testComment.updatedAt = LocalDate.now();
            issueCommentRepository.update(testComment).await().atMost(TIMEOUT);

            IssueCommentEntity edited = issueCommentRepository.findById(testComment.id())
                                                              .await().atMost(TIMEOUT);
            assertNotNull(edited.lastEditedAt(), "Comment should be marked as edited");
            assertNotNull(edited.updatedAt(), "Comment should have updated timestamp");
            assertEquals("Updated content", edited.body(), "Should have updated content");
        }
    }

    // Helper methods
    private void persistTestComments(ObjectId userId1, ObjectId userId2, ObjectId repoId1,
                                     ObjectId repoId2) {
        issueCommentRepository.persist(createTestComment(null, userId1, repoId1, Optional.of("1")))
                              .await().atMost(TIMEOUT);
        issueCommentRepository.persist(createTestComment(null, userId1, repoId2, Optional.of("2")))
                              .await().atMost(TIMEOUT);
        issueCommentRepository.persist(createTestComment(null, userId1, repoId2, Optional.of("3")))
                              .await().atMost(TIMEOUT);
        issueCommentRepository.persist(createTestComment(null, userId2, repoId1, Optional.of("4")))
                              .await().atMost(TIMEOUT);
        issueCommentRepository.persist(createTestComment(null, userId2, repoId2, Optional.of("5")))
                              .await().atMost(TIMEOUT);
    }

    private IssueCommentEntity createTestComment(ObjectId id, ObjectId userId, ObjectId repoId,
                                                 Optional<String> suffix) {
        var github = new Github();
        github.id = "test-github-" + suffix.orElse("id");
        github.url = URI.create(
                "https://github.com/test-user/test-repo/issues/issuecomment-" + suffix.orElse(
                        "id"));
        var userWithLogin = new UserWithLogin("test-user");
        var nameWithOwner = new NameWithOwner("test-repo", "test-user");

        var comment = new IssueCommentEntity();
        comment.id = id;
        comment.userId = userId;
        comment.repositoryId = repoId;
        comment.user = userWithLogin;
        comment.repository = nameWithOwner;
        comment.github = github;
        comment.createdAt = LocalDate.now();
        comment.updatedAt = LocalDate.now();
        comment.body = "Test comment body";

        return comment;
    }
}