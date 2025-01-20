package com.stergion.githubbackend.infrastructure.persistence.repositories;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.*;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("RepositoryRepository Tests")
class RepositoryRepositoryTest {

    @Inject
    RepositoryRepository repositoryRepository;

    private static final String TEST_OWNER = "test-owner";
    private static final String TEST_NAME = "test-repo";
    private Repository testRepository;

    @BeforeEach
    void setUp() {
        repositoryRepository.deleteAll();
        testRepository = createTestRepository(null, TEST_OWNER, TEST_NAME);
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("Should persist and retrieve repository by ID")
        void persistAndFindById() {
            repositoryRepository.persist(testRepository);

            assertNotNull(testRepository.id, "Repository ID should be generated");

            Repository found = repositoryRepository.findById(testRepository.id);
            assertNotNull(found, "Found repository should not be null");
            assertEquals(testRepository.id, found.id);
            assertEquals(testRepository.owner, found.owner);
            assertEquals(testRepository.name, found.name);
            assertEquals(testRepository.github.id, found.github.id);
        }

        @Test
        @DisplayName("Should delete repository")
        void deleteRepository() {
            repositoryRepository.persist(testRepository);

            repositoryRepository.delete(testRepository);

            assertNull(repositoryRepository.findById(testRepository.id),
                    "Repository should be deleted");
        }
    }

    @Nested
    @DisplayName("Search Operations")
    class SearchOperations {

        @Test
        @DisplayName("Should find repository by GitHub ID")
        void findByGitHubId() {
            repositoryRepository.persist(testRepository);

            Repository found = repositoryRepository.findByGitHubId(testRepository.github.id);
            assertNotNull(found, "Found repository should not be null");
            assertEquals(testRepository.id, found.id);
            assertEquals(testRepository.owner, found.owner);
            assertEquals(testRepository.name, found.name);
            assertEquals(testRepository.github.id, found.github.id);
        }

        @Test
        @DisplayName("Should find repositories by owner")
        void findByOwner() {
            // Create multiple repositories with the same owner
            Repository repo1 = createTestRepository(null, TEST_OWNER, "repo1");
            Repository repo2 = createTestRepository(null, TEST_OWNER, "repo2");
            Repository otherRepo = createTestRepository(null, "other-owner", "repo3");

            repositoryRepository.persist(repo1);
            repositoryRepository.persist(repo2);
            repositoryRepository.persist(otherRepo);

            List<Repository> found = repositoryRepository.findByOwner(TEST_OWNER);

            assertEquals(2, found.size(), "Should find two repositories");
            assertTrue(found.stream().allMatch(r -> r.owner.equals(TEST_OWNER)),
                    "All found repositories should have the test owner");
            assertTrue(found.stream().anyMatch(r -> r.name.equals("repo1")),
                    "Should find first repository");
            assertTrue(found.stream().anyMatch(r -> r.name.equals("repo2")),
                    "Should find second repository");
        }

        @Test
        @DisplayName("Should delete repositories by owner")
        void deleteByOwner() {
            // Create multiple repositories with the same owner
            Repository repo1 = createTestRepository(null, TEST_OWNER, "repo1");
            Repository repo2 = createTestRepository(null, TEST_OWNER, "repo2");
            Repository otherRepo = createTestRepository(null, "other-owner", "repo3");

            repositoryRepository.persist(repo1);
            repositoryRepository.persist(repo2);
            repositoryRepository.persist(otherRepo);

            repositoryRepository.deleteByOwner(TEST_OWNER);

            List<Repository> remainingRepos = repositoryRepository.listAll();
            assertEquals(1, remainingRepos.size(),
                    "Should only have one repository remaining");
            assertEquals("other-owner", remainingRepos.getFirst().owner,
                    "Remaining repository should be from other owner");
        }

        @Test
        @DisplayName("Should find repositories by ID list")
        void findByIdList() {
            ObjectId id1 = new ObjectId();
            ObjectId id2 = new ObjectId();

            Repository repo1 = createTestRepository(id1, "owner1", "name1");
            Repository repo2 = createTestRepository(id2, "owner2", "name2");

            repositoryRepository.persist(repo1);
            repositoryRepository.persist(repo2);

            List<Repository> found = repositoryRepository.findById(List.of(id1, id2));

            assertEquals(2, found.size(), "Should find both repositories");
            assertTrue(found.stream().anyMatch(r -> r.id.equals(id1)),
                    "Should find first repository");
            assertTrue(found.stream().anyMatch(r -> r.id.equals(id2)),
                    "Should find second repository");
        }

        @Test
        @DisplayName("Should return empty list when searching with empty ID list")
        void findByEmptyIdList() {
            List<Repository> found = repositoryRepository.findById(List.of());
            assertTrue(found.isEmpty(), "Should return empty list for empty ID list");
        }

        @Test
        @DisplayName("Should find repository by name and owner")
        void findByNameAndOwner() {
            repositoryRepository.persist(testRepository);

            Repository found = repositoryRepository.findByNameAndOwner(TEST_OWNER, TEST_NAME);

            assertNotNull(found, "Found repository should not be null");
            assertEquals(testRepository.owner, found.owner);
            assertEquals(testRepository.name, found.name);
        }

        @Test
        @DisplayName("Should find repositories by name and owners list")
        void findByNameAndOwners() {
            Repository repo1 = createTestRepository(null, "owner1", "name1");
            Repository repo2 = createTestRepository(null, "owner2", "name2");

            repositoryRepository.persist(repo1);
            repositoryRepository.persist(repo2);

            List<NameWithOwner> nameOwners = List.of(
                    new NameWithOwner("owner1", "name1"),
                    new NameWithOwner("owner2", "name2")
                                                    );

            List<Repository> found = repositoryRepository.findByNameAndOwners(nameOwners);

            assertEquals(2, found.size(), "Should find both repositories");
            assertTrue(found.stream().anyMatch(r ->
                            r.owner.equals("owner1") && r.name.equals("name1")),
                    "Should find first repository");
            assertTrue(found.stream().anyMatch(r ->
                            r.owner.equals("owner2") && r.name.equals("name2")),
                    "Should find second repository");
        }

        @Test
        @DisplayName("Should return empty list when searching with empty name and owners list")
        void findByEmptyNameAndOwners() {
            List<Repository> found = repositoryRepository.findByNameAndOwners(List.of());
            assertTrue(found.isEmpty(), "Should return empty list for empty name and owners list");
        }
    }

    @Nested
    @DisplayName("Repository Metadata Operations")
    class RepositoryMetadataOperations {

        @Test
        @DisplayName("Should handle repository with languages")
        void handleRepositoryLanguages() {
            Language language = new Language();
            language.name = "Java";
            language.size = 1000;
            language.percentage = 100.0f;

            testRepository.primaryLanguage = "Java";
            testRepository.languages = List.of(language);
            testRepository.languagesCount = 1;
            testRepository.languagesSize = 1000;

            repositoryRepository.persist(testRepository);

            Repository found = repositoryRepository.findById(testRepository.id);
            assertEquals("Java", found.primaryLanguage, "Primary language should match");
            assertEquals(1, found.languages.size(), "Should have one language");
            assertEquals("Java", found.languages.getFirst().name, "Language name should match");
            assertEquals(1000, found.languagesSize, "Language size should match");
        }

        @Test
        @DisplayName("Should handle repository with topics")
        void handleRepositoryTopics() {
            Topic topic = new Topic();
            topic.name = "spring-boot";

            testRepository.topics = List.of(topic);
            testRepository.topicsCount = 1;

            repositoryRepository.persist(testRepository);

            Repository found = repositoryRepository.findById(testRepository.id);
            assertEquals(1, found.topics.size(), "Should have one topic");
            assertEquals("spring-boot", found.topics.getFirst().name, "Topic name should match");
        }

        @Test
        @DisplayName("Should handle repository with labels")
        void handleRepositoryLabels() {
            Label label = new Label("bug", "Bug label");
            testRepository.labels = List.of(label);
            testRepository.labelsCount = 1;

            repositoryRepository.persist(testRepository);

            Repository found = repositoryRepository.findById(testRepository.id);
            assertEquals(1, found.labels.size(), "Should have one label");
            assertEquals("bug", found.labels.getFirst().name(), "Label name should match");
            assertEquals("Bug label", found.labels.getFirst().description(),
                    "Label description should match");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null values in optional fields")
        void handleNullOptionalFields() {
            Repository repoWithNulls = createTestRepository(null, TEST_OWNER, TEST_NAME);
            repoWithNulls.labels = null;
            repoWithNulls.languages = null;
            repoWithNulls.topics = null;
            repoWithNulls.primaryLanguage = null;

            repositoryRepository.persist(repoWithNulls);

            Repository found = repositoryRepository.findById(repoWithNulls.id);
            assertNotNull(found, "Found repository should not be null");
            assertNull(found.labels, "Labels should be null");
            assertNull(found.languages, "Languages should be null");
            assertNull(found.topics, "Topics should be null");
            assertNull(found.primaryLanguage, "Primary language should be null");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5})
        @DisplayName("Should handle different metadata counts")
        void handleDifferentMetadataCounts(int count) {
            Repository repository = createTestRepository(null, TEST_OWNER, TEST_NAME);

            // Add varying numbers of labels
            List<Label> labels = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                labels.add(new Label("label" + i, "description" + i));
            }
            repository.labels = labels;
            repository.labelsCount = count;

            repositoryRepository.persist(repository);

            Repository found = repositoryRepository.findById(repository.id);
            assertEquals(count, found.labels.size(),
                    "Should find correct number of labels");
        }
    }

    // Helper methods
    private Repository createTestRepository(ObjectId id, String owner, String name) {
        var github = new Github();
        github.id = "test-github-id";

        var repository = new Repository();
        repository.id = id;
        repository.owner = owner;
        repository.name = name;
        repository.github = github;
        repository.labelsCount = 0;
        repository.languagesCount = 0;
        repository.languagesSize = 0;
        repository.topicsCount = 0;
        repository.forkCount = 0;
        repository.stargazerCount = 0;
        repository.watcherCount = 0;

        return repository;
    }
}