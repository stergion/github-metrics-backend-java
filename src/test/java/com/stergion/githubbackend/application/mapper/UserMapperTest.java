package com.stergion.githubbackend.application.mapper;

import com.stergion.githubbackend.application.response.UserResponse;
import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserMapperTest {

    @Inject
    UserMapper userMapper;

    private User testUser;
    private List<Repository> testRepositories;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Setup test user
        var repo1Id = new ObjectId();
        var repo2Id = new ObjectId();
        testUser = new User(
                new ObjectId(),
                "testuser",
                "Test User",
                new Github("123", URI.create("https://github.com/testuser")),
                "test@example.com",
                now,
                now,
                List.of(repo1Id, repo2Id),
                URI.create("https://avatar.url"),
                "Test bio",
                "testhandle",
                URI.create("https://website.url")
        );

        // Setup test repositories
        testRepositories = List.of(
                new Repository(
                        repo1Id,  // Use the same ID as in user's repositories list
                        "testuser",
                        "repo1",
                        new Github("456", URI.create("https://github.com/testuser/repo1")),
                        List.of(),
                        0,
                        "Java",
                        List.of(),
                        1,
                        1000,
                        List.of(),
                        0,
                        10,
                        20,
                        5
                ),
                new Repository(
                        repo2Id,  // Use the same ID as in user's repositories list
                        "testuser",
                        "repo2",
                        new Github("789", URI.create("https://github.com/testuser/repo2")),
                        List.of(),
                        0,
                        "Python",
                        List.of(),
                        1,
                        2000,
                        List.of(),
                        0,
                        20,
                        30,
                        8
                )
                                  );
    }

    @Test
    void testToResponse_withValidData() {
        // First verify that our test data is set up correctly
        assertEquals(2, testUser.repositories().size());
        assertEquals(2, testRepositories.size());
        assertTrue(testUser.repositories().contains(testRepositories.get(0).id()));
        assertTrue(testUser.repositories().contains(testRepositories.get(1).id()));
        UserResponse response = userMapper.toResponse(testUser, testRepositories);

        assertNotNull(response);
        assertEquals(testUser.login(), response.login());
        assertEquals(testUser.name(), response.name());
        assertEquals(testUser.github().id(), response.github().id());
        assertEquals(testUser.github().url(), response.github().url());
        assertEquals(testUser.email(), response.email());
        assertEquals(testUser.avatarURL(), response.avatarURL());
        assertEquals(testUser.bio(), response.bio());
        assertEquals(testUser.twitterHandle(), response.twitterHandle());
        assertEquals(testUser.websiteURL(), response.websiteURL());

        // Verify repositories mapping
        assertEquals(2, response.repositories().size());
        List<NameWithOwner> repositories = response.repositories();

        assertTrue(repositories.stream()
                               .anyMatch(repo -> repo.owner().equals("testuser") && repo.name().equals("repo1")));
        assertTrue(repositories.stream()
                               .anyMatch(repo -> repo.owner().equals("testuser") && repo.name().equals("repo2")));
    }

    @Test
    void testToResponse_repositoryMappingVerification() {
        // Create a repository that's not in the user's repository list
        ObjectId nonUserRepoId = new ObjectId();
        Repository nonUserRepo = new Repository(
                nonUserRepoId,
                "otheruser",
                "otherrepo",
                new Github("999", URI.create("https://github.com/otheruser/otherrepo")),
                List.of(),
                0,
                "Java",
                List.of(),
                1,
                1000,
                List.of(),
                0,
                10,
                20,
                5
        );

        // Create a list with all repositories including the one not in user's list
        List<Repository> allRepositories = new java.util.ArrayList<>(testRepositories);
        allRepositories.add(nonUserRepo);

        // Perform the mapping
        UserResponse response = userMapper.toResponse(testUser, allRepositories);

        // Verify that only repositories from user's repository list are mapped
        assertEquals(2, response.repositories().size());

        // Verify that each repository in the response matches a repository ID from the user's list
        for (NameWithOwner mappedRepo : response.repositories()) {
            boolean foundMatchingRepo = testUser.repositories().stream()
                                                .anyMatch(repoId ->
                                                                testRepositories.stream()
                                                                                .anyMatch(repo ->
                                                                                                repo.id().equals(repoId)
                                                                                                        && repo.owner().equals(mappedRepo.owner())
                                                                                                        && repo.name().equals(mappedRepo.name())
                                                                                         )
                                                         );
            assertTrue(foundMatchingRepo, "Mapped repository should match one from user's repository list");
        }

        // Verify that the non-user repository is not included in the mapping
        assertFalse(response.repositories().stream()
                            .anyMatch(repo -> repo.owner().equals("otheruser") && repo.name().equals("otherrepo")),
                "Repository not in user's list should not be included in mapping");
    }

    @Test
    void testToResponse_withNullRepositories() {
        UserResponse response = userMapper.toResponse(testUser, null);

        assertNotNull(response);
        assertTrue(response.repositories().isEmpty());
    }

    @Test
    void testToResponse_withEmptyRepositories() {
        UserResponse response = userMapper.toResponse(testUser, List.of());

        assertNotNull(response);
        assertTrue(response.repositories().isEmpty());
    }

    @Test
    void testToResponse_withNullOptionalFields() {
        User userWithNulls = new User(
                new ObjectId(),
                "testuser",
                "Test User",
                new Github("123", URI.create("https://github.com/testuser")),
                "test@example.com",
                now,
                now,
                List.of(),
                null,  // null avatarURL
                null,  // null bio
                null,  // null twitterHandle
                null   // null websiteURL
        );

        UserResponse response = userMapper.toResponse(userWithNulls, testRepositories);

        assertNotNull(response);
        assertNull(response.avatarURL());
        assertNull(response.bio());
        assertNull(response.twitterHandle());
        assertNull(response.websiteURL());
    }

    @Test
    void testToResponse_verifyGithubUrlCopy() {
        UserResponse response = userMapper.toResponse(testUser, testRepositories);

        assertNotNull(response.github());
        assertEquals(testUser.github().url(), response.github().url());
    }
}
