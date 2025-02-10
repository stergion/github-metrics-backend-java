package com.stergion.githubbackend.application.mapper;

import com.stergion.githubbackend.application.response.NameWithOwnerResponse;
import com.stergion.githubbackend.application.response.RepositoryResponse;
import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.domain.utils.types.Language;
import com.stergion.githubbackend.domain.utils.types.Topic;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("Repository Mapper Tests")
class RepositoryMapperTest {

    @Inject
    RepositoryMapper repositoryMapper;

    @Nested
    @DisplayName("Repository Response Mapping Tests")
    class RepositoryResponseTests {

        @Test
        @DisplayName("Should map all fields correctly when DTO is complete")
        void shouldMapAllFieldsCorrectly() {
            // Arrange
            var id = new ObjectId();
            var owner = "testOwner";
            var name = "testRepo";
            var github = new Github("123", URI.create("https://github.com/testOwner/testRepo"));
            var labels = List.of(new Label("bug", "Its a bug"),
                    new Label("enhancement", "Its an enhancement"));
            var languages = List.of(new Language("Java", 1000, 0.4f),
                    new Language("Python", 500, 0.6f));
            var topics = List.of(new Topic("spring"), new Topic("web"));

            var dto = new Repository(
                    id,
                    owner,
                    name,
                    github,
                    labels,
                    2,
                    "Java",
                    languages,
                    2,
                    1500,
                    topics,
                    2,
                    10,
                    100,
                    50
            );

            // Act
            RepositoryResponse response = repositoryMapper.toRepositoryResponse(dto);

            // Assert
            assertNotNull(response, "Response should not be null");
            assertEquals(owner, response.owner(), "Owner should match");
            assertEquals(name, response.name(), "Name should match");
            assertEquals(github, response.github(), "Github object should match");

            // Check collections
            assertIterableEquals(labels, response.labels(), "Labels should match");
            assertIterableEquals(languages, response.languages(), "Languages should match");
            assertIterableEquals(topics, response.topics(), "Topics should match");

            // Check counts and metrics
            assertEquals(2, response.labelsCount(), "Labels count should match");
            assertEquals("Java", response.primaryLanguage(), "Primary language should match");
            assertEquals(2, response.languagesCount(), "Languages count should match");
            assertEquals(1500, response.languagesSize(), "Languages size should match");
            assertEquals(2, response.topicsCount(), "Topics count should match");
            assertEquals(10, response.forkCount(), "Fork count should match");
            assertEquals(100, response.stargazerCount(), "Stargazer count should match");
            assertEquals(50, response.watcherCount(), "Watcher count should match");
        }

        @Test
        @DisplayName("Should handle null collections by returning empty lists")
        void shouldHandleNullCollections() {
            // Arrange
            var id = new ObjectId();
            var dto = new Repository(
                    id,
                    "owner",
                    "name",
                    new Github("123", URI.create("https://github.com/owner/name")),
                    null,
                    0,
                    "Java",
                    null,
                    0,
                    0,
                    null,
                    0,
                    0,
                    0,
                    0
            );

            // Act
            RepositoryResponse response = repositoryMapper.toRepositoryResponse(dto);

            // Assert
            assertNotNull(response, "Response should not be null");
            assertTrue(response.labels().isEmpty(), "Labels should be empty list");
            assertTrue(response.languages().isEmpty(), "Languages should be empty list");
            assertTrue(response.topics().isEmpty(), "Topics should be empty list");
        }

        @Test
        @DisplayName("Should handle null primary language")
        void shouldHandleNullPrimaryLanguage() {
            // Arrange
            var id = new ObjectId();
            var dto = new Repository(
                    id,
                    "owner",
                    "name",
                    new Github("123", URI.create("https://github.com/owner/name")),
                    List.of(),
                    0,
                    null,
                    List.of(),
                    0,
                    0,
                    List.of(),
                    0,
                    0,
                    0,
                    0
            );

            // Act
            RepositoryResponse response = repositoryMapper.toRepositoryResponse(dto);

            // Assert
            assertNotNull(response, "Response should not be null");
            assertNull(response.primaryLanguage(), "Primary language should be null");
        }
    }

    @Nested
    @DisplayName("Name With Owner Response Mapping Tests")
    class NameWithOwnerResponseTests {

        @Test
        @DisplayName("Should map owner and name correctly")
        void shouldMapOwnerAndNameCorrectly() {
            // Arrange
            var id = new ObjectId();
            var owner = "testOwner";
            var name = "testRepo";
            var dto = new Repository(
                    id,
                    owner,
                    name,
                    new Github("123", URI.create("https://github.com/testOwner/testRepo")),
                    List.of(),
                    0,
                    null,
                    List.of(),
                    0,
                    0,
                    List.of(),
                    0,
                    0,
                    0,
                    0
            );

            // Act
            NameWithOwnerResponse response = repositoryMapper.toNameWithOwnerResponse(dto);

            // Assert
            assertAll(
                    () -> assertNotNull(response, "Response should not be null"),
                    () -> assertEquals(owner, response.owner(), "Owner should match"),
                    () -> assertEquals(name, response.name(), "Name should match")
                     );
        }
    }
}