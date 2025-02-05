package com.stergion.githubbackend.infrastructure.persistence.mongo.users;

import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Github;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = createTestUser(null);
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("Should persist and retrieve user by ID")
        void persistAndFindById() {
            userRepository.persist(testUser);

            assertNotNull(testUser.id, "User ID should be generated");

            User found = userRepository.findById(testUser.id);
            assertNotNull(found, "Found user should not be null");
            assertEquals(testUser.id, found.id);
            assertEquals(testUser.login, found.login);
            assertEquals(testUser.name, found.name);
            assertEquals(testUser.github.id, found.github.id);
            assertEquals(testUser.email, found.email);
        }

        @Test
        @DisplayName("Should delete user by ID")
        void deleteById() {
            userRepository.persist(testUser);
            ObjectId userId = testUser.id;

            userRepository.deleteById(userId);

            assertNull(userRepository.findById(userId), "User should be deleted");
        }

        @Test
        @DisplayName("Should update existing user")
        void updateUser() {
            userRepository.persist(testUser);

            testUser.name = "Updated Name";
            testUser.email = "updated@email.com";
            userRepository.update(testUser);

            User updated = userRepository.findById(testUser.id);
            assertEquals("Updated Name", updated.name, "Name should be updated");
            assertEquals("updated@email.com", updated.email, "Email should be updated");
        }
    }

    @Nested
    @DisplayName("Search Operations")
    class SearchOperations {

        @Test
        @DisplayName("Should find user by GitHub ID")
        void findByGitHubId() {
            userRepository.persist(testUser);

            User found = userRepository.findByGitHubId(testUser.github.id);
            assertNotNull(found, "Found user should not be null");
            assertEquals(testUser.id, found.id);
            assertEquals(testUser.login, found.login);
            assertEquals(testUser.github.id, found.github.id);
        }

        @Test
        @DisplayName("Should find user by login")
        void findByLogin() {
            userRepository.persist(testUser);

            User found = userRepository.findByLogin(testUser.login);
            assertNotNull(found, "Found user should not be null");
            assertEquals(testUser.id, found.id);
            assertEquals(testUser.login, found.login);
        }

        @Test
        @DisplayName("Should find users by email")
        void findByEmail() {
            // Create multiple users with the same email
            User user1 = createTestUser(null);
            User user2 = createTestUser(null);
            user2.login = "test-user-2";
            user2.name = "Test User 2";
            // Keep the same email for both users

            userRepository.persist(user1);
            userRepository.persist(user2);

            List<User> found = userRepository.findByEmail(user1.email);

            assertNotNull(found, "Found users should not be null");
            assertEquals(2, found.size(), "Should find both users with the same email");
            assertTrue(found.stream().anyMatch(u -> u.login.equals("test-user")),
                    "Should find first test user");
            assertTrue(found.stream().anyMatch(u -> u.login.equals("test-user-2")),
                    "Should find second test user");
            found.forEach(user -> assertEquals(user1.email, user.email,
                    "All found users should have the same email"));
        }
    }

    @Nested
    @DisplayName("Repository Operations")
    class RepositoryOperations {

        @Test
        @DisplayName("Should add repository to user's repositories")
        void addRepository() {
            userRepository.persist(testUser);
            ObjectId newRepoId = new ObjectId();

            testUser.repositories.add(newRepoId);
            userRepository.update(testUser);

            User updated = userRepository.findById(testUser.id);
            assertTrue(updated.repositories.contains(newRepoId),
                    "User's repositories should contain the new repository ID");
        }

        @Test
        @DisplayName("Should remove repository from user's repositories")
        void removeRepository() {
            ObjectId repoId = new ObjectId();
            testUser.repositories = List.of(repoId);
            userRepository.persist(testUser);

            testUser.repositories = List.of();
            userRepository.update(testUser);

            User updated = userRepository.findById(testUser.id);
            assertFalse(updated.repositories.contains(repoId),
                    "User's repositories should not contain the removed repository ID");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null values in optional fields")
        void handleNullOptionalFields() {
            User userWithNulls = createTestUser(null);
            userWithNulls.bio = null;
            userWithNulls.twitterHandle = null;
            userWithNulls.websiteURL = null;
            userWithNulls.avatarURL = null;

            userRepository.persist(userWithNulls);

            User found = userRepository.findById(userWithNulls.id);
            assertNotNull(found, "Found user should not be null");
            assertNull(found.bio, "Bio should be null");
            assertNull(found.twitterHandle, "Twitter handle should be null");
            assertNull(found.websiteURL, "Website URL should be null");
            assertNull(found.avatarURL, "Avatar URL should be null");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5})
        @DisplayName("Should handle different repository counts")
        void handleDifferentRepositoryCounts(int repoCount) {
            User user = createTestUser(null);
            user.repositories = new ArrayList<>();
            for (int i = 0; i < repoCount; i++) {
                user.repositories.add(new ObjectId());
            }

            userRepository.persist(user);

            User found = userRepository.findById(user.id);
            assertEquals(repoCount, found.repositories.size(),
                    "Should find correct number of repositories");
        }
    }

    @Nested
    @DisplayName("User Profile Operations")
    class UserProfileOperations {

        @Test
        @DisplayName("Should update user profile information")
        void updateUserProfile() {
            userRepository.persist(testUser);

            // Update profile information
            testUser.bio = "Updated bio";
            testUser.twitterHandle = "new_handle";
            testUser.websiteURL = URI.create("https://newwebsite.com");
            testUser.avatarURL = URI.create("https://new-avatar-url.com");
            testUser.updatedAt = LocalDateTime.now();

            userRepository.update(testUser);

            User updated = userRepository.findById(testUser.id);
            assertEquals("Updated bio", updated.bio, "Bio should be updated");
            assertEquals("new_handle", updated.twitterHandle, "Twitter handle should be updated");
            assertEquals(URI.create("https://newwebsite.com"), updated.websiteURL,
                    "Website URL should be updated");
            assertEquals(URI.create("https://new-avatar-url.com"), updated.avatarURL,
                    "Avatar URL should be updated");
            assertTrue(
                    Duration.between(LocalDateTime.now(), updated.updatedAt).getSeconds() < 5,
                    "Updated date should be set to today"
                      );
        }

        @Nested
        @DisplayName("Date Management")
        class DateManagement {

            @Test
            @DisplayName("Should set both creation and update dates on persist")
            void shouldSetBothDatesOnPersist() {
                testUser.createdAt = null;
                testUser.updatedAt = null;
                userRepository.persist(testUser);

                User persisted = userRepository.findById(testUser.id);
                assertNotNull(persisted.createdAt, "Creation date should be automatically set");
                assertNotNull(persisted.updatedAt, "Update date should be automatically set");
                assertTrue(
                        Duration.between(LocalDateTime.now(), persisted.createdAt).getSeconds() < 5,
                        "Creation date should be set to current date"
                          );
                assertEquals(persisted.createdAt, persisted.updatedAt,
                        "Update date should equal creation date for new entities");
            }

            @Test
            @DisplayName("Should set creation date on batch persist")
            void shouldSetCreationDateOnBatchPersist() {
                User user1 = createTestUser(null);
                User user2 = createTestUser(null);
                user1.createdAt = null;
                user2.createdAt = null;
                user2.login = "test-user-2";

                userRepository.persist(List.of(user1, user2));

                List<User> persisted = userRepository.listAll();
                assertEquals(2, persisted.size(), "Should persist both users");
                persisted.forEach(user -> {
                    assertNotNull(user.createdAt, "Creation date should be set for all users");
                    assertTrue(
                            Duration.between(LocalDateTime.now(), user.createdAt).getSeconds() < 5,
                            "Creation date should be close to test start time"
                              );
                });
            }

            @Test
            @DisplayName("Should handle persistOrUpdate for new entity")
            void shouldHandlePersistOrUpdateForNew() {
                testUser.createdAt = null;
                userRepository.persistOrUpdate(testUser);

                User persisted = userRepository.findById(testUser.id);
                assertNotNull(persisted.createdAt, "Creation date should be set for new entity");
                assertTrue(
                        Duration.between(LocalDateTime.now(), persisted.createdAt).getSeconds() < 5,
                        "Creation date should be current date for new entity"
                          );
            }

            @Test
            @DisplayName("Should handle persistOrUpdate for existing entity")
            void shouldHandlePersistOrUpdateForExisting() {
                // First persist
                userRepository.persist(testUser);
                LocalDateTime originalCreationDate = testUser.createdAt;
                LocalDateTime originalUpdateDate = testUser.updatedAt;

                // Wait a tiny bit to ensure different timestamps
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // Ignore
                }

                // Then try to update
                testUser.name = "Updated Name";
                testUser.createdAt = LocalDateTime.now().plusDays(1); // Try to modify creation date
                userRepository.persistOrUpdate(testUser);

                User updated = userRepository.findById(testUser.id);
                assertTrue(
                        Duration.between(originalCreationDate, updated.createdAt).getSeconds() < 5,
                        "Creation date should be preserved for existing entity"
                          );
                assertNotEquals(originalUpdateDate, updated.updatedAt,
                        "Update date should be modified");
                assertTrue(
                        Duration.between(LocalDateTime.now(), updated.updatedAt).getSeconds() < 5,
                        "Update date should be set to current date"
                          );
            }

            @Test
            @DisplayName("Should handle batch persistOrUpdate")
            void shouldHandleBatchPersistOrUpdate() {
                // Create one existing and one new user
                userRepository.persist(testUser);
                LocalDateTime originalDate = testUser.createdAt;

                User newUser = createTestUser(null);
                newUser.login = "test-user-2";
                newUser.createdAt = null;

                userRepository.persistOrUpdate(List.of(testUser, newUser));

                User existingUpdated = userRepository.findById(testUser.id);
                assertTrue(
                        Duration.between(originalDate, existingUpdated.createdAt).getSeconds() < 5,
                        "Should preserve creation date for existing user"
                          );

                User newPersisted = userRepository.findByLogin("test-user-2");
                assertNotNull(newPersisted.createdAt,
                        "Should set creation date for new user");
                assertTrue(
                        Duration.between(LocalDateTime.now(), newPersisted.createdAt)
                                .getSeconds() < 5,
                        "Should set current date for new user");
            }
        }
    }

    // Helper methods
    private User createTestUser(ObjectId id) {
        var github = new Github();
        github.id = "test-github-id";
        github.url = URI.create("https://github.com/test-user");

        var user = new User();
        user.id = id;
        user.login = "test-user";
        user.name = "Test User";
        user.github = github;
        user.email = "test@example.com";
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        user.repositories = new ArrayList<>();
        user.avatarURL = URI.create("https://avatar-url.com");
        user.bio = "Test bio";
        user.twitterHandle = "test_handle";
        user.websiteURL = URI.create("https://test-website.com");

        return user;
    }
}