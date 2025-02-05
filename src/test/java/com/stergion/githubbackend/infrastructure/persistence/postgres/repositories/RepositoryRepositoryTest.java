package com.stergion.githubbackend.infrastructure.persistence.postgres.repositories;

import com.stergion.githubbackend.infrastructure.persistence.postgres.TestEntityCreators;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Label;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Language;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Topic;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("Repository Repository Tests")
class RepositoryRepositoryTest {

    @Inject
    RepositoryRepository repositoryRepository;

    @BeforeEach
    @RunOnVertxContext
    void setUp(UniAsserter asserter) {
        asserter.execute(() -> Panache.withTransaction(() -> repositoryRepository.deleteAll()));
        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @RunOnVertxContext
        @DisplayName("Should update existing repository")
        void updateRepository(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("testRepo");

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            repo.setStargazerCount(100);
            repo.setForkCount(50);
            repo.setWatcherCount(25);

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    updatedRepo -> {
                        assertEquals(100, updatedRepo.getStargazerCount());
                        assertEquals(50, updatedRepo.getForkCount());
                        assertEquals(25, updatedRepo.getWatcherCount());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete existing repository")
        void deleteRepository(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("testRepo");

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));
            asserter.execute(() -> Panache.withTransaction(
                    () -> repositoryRepository.deleteById(repo.getId())));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should not allow duplicate repositories with same owner and name")
        void noDuplicateRepositories(UniAsserter asserter) {
            Repository repo1 = TestEntityCreators.createTestRepository("testRepo", "testOwner");
            Repository repo2 = TestEntityCreators.createTestRepository("testRepo", "testOwner");

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo1)));

            asserter.assertFailedWith(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo2)),
                    throwable -> assertTrue(
                            throwable.getMessage().toLowerCase().contains("constraint"))
                                     );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should create a new repository successfully")
        void createRepository(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("testRepo");

            asserter.assertThat(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)),
                    persistedRepo -> {
                        assertNotNull(persistedRepo.getId());
                        assertEquals(repo.getName(), persistedRepo.getName());
                        assertEquals(repo.getOwner(), persistedRepo.getOwner());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find repository by ID")
        void findRepositoryById(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("testRepo");

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    foundRepo -> {
                        assertNotNull(foundRepo);
                        assertEquals(repo.getName(), foundRepo.getName());
                        assertEquals(repo.getOwner(), foundRepo.getOwner());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Custom Finder Methods")
    class CustomFinderTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle mixed existing and non-existing repositories in batch find")
        void findByNameAndOwnersMixed(UniAsserter asserter) {
            List<Repository> repos = List.of(
                    TestEntityCreators.createTestRepository("repo1", "owner1"),
                    TestEntityCreators.createTestRepository("repo1", "owner2"),
                    TestEntityCreators.createTestRepository("repo1", "owner3"),
                    TestEntityCreators.createTestRepository("repo1", "owner4"),
                    TestEntityCreators.createTestRepository("repo2", "owner1"),
                    TestEntityCreators.createTestRepository("repo2", "owner2"),
                    TestEntityCreators.createTestRepository("repo2", "owner3"),
                    TestEntityCreators.createTestRepository("repo2", "owner4"),
                    TestEntityCreators.createTestRepository("repo3", "owner1"),
                    TestEntityCreators.createTestRepository("repo3", "owner2")
                                            );

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repos)));

            List<NameWithOwner> nameWithOwners = List.of(
                    new NameWithOwner("owner1", "repo1"),
                    new NameWithOwner("owner1", "repo2"),
                    new NameWithOwner("owner2", "repo1"),
                    new NameWithOwner("owner2", "nonexistent")
                                                        );

            asserter.assertThat(
                    () -> repositoryRepository.findByNameAndOwners(nameWithOwners)
                                              .invoke(i -> System.out.println("found: " + i)),
                    foundRepos -> {
                        assertEquals(3, foundRepos.size());
                        assertTrue(foundRepos.stream()
                                             .anyMatch(
                                                     r -> r.getOwner().equals("owner1")
                                                             && r.getName().equals("repo1")
                                                      ));
                        assertTrue(foundRepos.stream()
                                             .anyMatch(
                                                     r -> r.getOwner().equals("owner1")
                                                             && r.getName().equals("repo2")
                                                      ));
                        assertTrue(foundRepos.stream()
                                             .anyMatch(
                                                     r -> r.getOwner().equals("owner2")
                                                             && r.getName().equals("repo1")
                                                      ));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle case sensitivity in owner and name searches")
        void caseSensitiveSearch(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("RepoName", "OwnerName");

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            // Test exact match
            asserter.assertThat(
                    () -> repositoryRepository.findByNameAndOwner("OwnerName", "RepoName"),
                    foundRepo -> assertNotNull(foundRepo)
                               );

            // Test case mismatch
            asserter.assertThat(
                    () -> repositoryRepository.findByNameAndOwner("ownerName", "repoName"),
                    foundRepo -> assertNull(foundRepo)
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find repository by GitHub ID")
        void findByGitHubId(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("testRepo");

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findByGitHubId(repo.getGithubId()),
                    foundRepo -> {
                        assertNotNull(foundRepo);
                        assertEquals(repo.getGithubId(), foundRepo.getGithubId());
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> repositoryRepository.findByGitHubId("nonexistent"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find repositories by owner")
        void findByOwner(UniAsserter asserter) {
            List<Repository> repos = new ArrayList<>();
            repos.add(TestEntityCreators.createTestRepository("repo1", "owner1"));
            repos.add(TestEntityCreators.createTestRepository("repo2", "owner1"));
            repos.add(TestEntityCreators.createTestRepository("repo3", "owner2"));

            asserter.execute(() -> Panache.withTransaction(() ->
                            repositoryRepository.persist(repos)
                                                          ));

            asserter.assertThat(
                    () -> repositoryRepository.findByOwner("owner1"),
                    foundRepos -> {
                        assertEquals(2, foundRepos.size());
                        assertTrue(
                                foundRepos.stream().allMatch(r -> r.getOwner().equals("owner1")));
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> repositoryRepository.findByOwner("nonexistent"),
                    foundRepos -> assertTrue(foundRepos.isEmpty())
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find repository by name and owner")
        void findByNameAndOwner(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("specific-repo",
                    "specific-owner");

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findByNameAndOwner("specific-owner",
                            "specific-repo"),
                    foundRepo -> {
                        assertNotNull(foundRepo);
                        assertEquals("specific-repo", foundRepo.getName());
                        assertEquals("specific-owner", foundRepo.getOwner());
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> repositoryRepository.findByNameAndOwner("wrong-owner", "wrong-repo"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find repositories by name and owners list")
        void findByNameAndOwners(UniAsserter asserter) {
            List<Repository> repos = new ArrayList<>();
            repos.add(TestEntityCreators.createTestRepository("repo1", "owner1"));
            repos.add(TestEntityCreators.createTestRepository("repo2", "owner2"));

            asserter.execute(() -> Panache.withTransaction(() ->
                            repositoryRepository.persist(repos)
                                                          ));

            List<NameWithOwner> nameWithOwners = List.of(
                    new NameWithOwner("owner1", "repo1"),
                    new NameWithOwner("owner2", "repo2")
                                                        );

            asserter.assertThat(
                    () -> repositoryRepository.findByNameAndOwners(nameWithOwners),
                    foundRepos -> {
                        assertEquals(2, foundRepos.size());
                        assertTrue(foundRepos.stream()
                                             .anyMatch(r -> r.getName()
                                                             .equals("repo1") && r.getOwner()
                                                                                  .equals("owner1"
                                                                                         )));
                        assertTrue(foundRepos.stream()
                                             .anyMatch(r -> r.getName()
                                                             .equals("repo2") && r.getOwner()
                                                                                  .equals("owner2"
                                                                                         )));
                    }
                               );

            // Test empty list case
            asserter.assertThat(
                    () -> repositoryRepository.findByNameAndOwners(List.of()),
                    foundRepos -> assertTrue(foundRepos.isEmpty())
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperationsTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete repositories by owner")
        void deleteByOwner(UniAsserter asserter) {
            List<Repository> repos = new ArrayList<>();
            repos.add(TestEntityCreators.createTestRepository("repo1", "owner-to-delete"));
            repos.add(TestEntityCreators.createTestRepository("repo2", "owner-to-delete"));
            repos.add(TestEntityCreators.createTestRepository("repo3", "owner-to-keep"));

            asserter.execute(() -> Panache.withTransaction(() ->
                            repositoryRepository.persist(repos)
                                                          ));

            asserter.execute(() -> Panache.withTransaction(() ->
                            repositoryRepository.deleteByOwner("owner-to-delete")
                                                          ));

            asserter.assertThat(
                    () -> repositoryRepository.findByOwner("owner-to-delete"),
                    deletedRepos -> assertTrue(deletedRepos.isEmpty())
                               );

            asserter.assertThat(
                    () -> repositoryRepository.findByOwner("owner-to-keep"),
                    keptRepos -> assertEquals(1, keptRepos.size())
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Relationship Tests")
    class RelationshipTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle repository with labels")
        void repositoryWithLabels(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("labelTest");

            // Create and add labels
            Label label1 = new Label();
            label1.setLabel("bug");
            label1.setDescription("Bug label");

            Label label2 = new Label();
            label2.setLabel("feature");
            label2.setDescription("Feature label");

            repo.setLabels(List.of(label1, label2));
            repo.setLabelsCount(2);

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    foundRepo -> {
                        assertEquals(2, foundRepo.getLabelsCount());
                        assertEquals(2, foundRepo.getLabels().size());
                        assertTrue(foundRepo.getLabels().stream()
                                            .anyMatch(l -> l.getLabel().equals("bug")));
                        assertTrue(foundRepo.getLabels().stream()
                                            .anyMatch(l -> l.getLabel().equals("feature")));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle repository with languages")
        void repositoryWithLanguages(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("languageTest");

            Language java = new Language();
            java.setName("Java");
            java.setSize(1000);
            java.setPercentage(70.0f);

            Language python = new Language();
            python.setName("Python");
            python.setSize(428);
            python.setPercentage(30.0f);

            repo.setLanguages(List.of(java, python));
            repo.setLanguagesCount(2);
            repo.setLanguagesSize(1428);
            repo.setPrimaryLanguage("Java");

            asserter.execute(() -> Panache.withTransaction(() ->
                            repositoryRepository.persist(repo)
                                                          ));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    foundRepo -> {
                        assertEquals(2, foundRepo.getLanguagesCount());
                        assertEquals(1428, foundRepo.getLanguagesSize());
                        assertEquals("Java", foundRepo.getPrimaryLanguage());
                        assertEquals(2, foundRepo.getLanguages().size());
                        assertTrue(foundRepo.getLanguages().stream()
                                            .anyMatch(l -> l.getName()
                                                            .equals("Java") && l.getPercentage() == 70.0f));
                        assertTrue(foundRepo.getLanguages().stream()
                                            .anyMatch(l -> l.getName()
                                                            .equals("Python") && l.getPercentage() == 30.0f));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update repository relationships")
        void updateRepositoryRelationships(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("relationshipTest");

            // Initial labels
            Label label1 = new Label();
            label1.setLabel("bug");
            repo.setLabels(List.of(label1));
            repo.setLabelsCount(1);

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            // Add another label
            Label label2 = new Label();
            label2.setLabel("feature");
            repo.setLabels(List.of(label1, label2));
            repo.setLabelsCount(2);

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    foundRepo -> {
                        assertEquals(2, foundRepo.getLabelsCount());
                        assertEquals(2, foundRepo.getLabels().size());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should cascade delete repository relationships")
        void cascadeDeleteRelationships(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("cascadeTest");

            // Add labels and languages
            Label label = new Label();
            label.setLabel("bug");
            repo.setLabels(List.of(label));

            Language java = new Language();
            java.setName("Java");
            java.setSize(1000);
            java.setPercentage(100.0f);
            repo.setLanguages(List.of(java));

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));
            asserter.execute(() -> Panache.withTransaction(
                    () -> repositoryRepository.deleteById(repo.getId())));

            // Verify cascade delete worked
            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should validate relationship consistency")
        void validateRelationshipConsistency(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("consistencyTest");

            // Set inconsistent state
            Label label = new Label();
            label.setLabel("bug");
            repo.setLabels(List.of(label));
            repo.setLabelsCount(2); // Incorrect count

            Language java = new Language();
            java.setName("Java");
            java.setSize(1000);
            java.setPercentage(70.0f);
            Language python = new Language();
            python.setName("Python");
            python.setSize(1000);
            python.setPercentage(40.0f); // Total percentage > 100
            repo.setLanguages(List.of(java, python));

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    foundRepo -> {
                        // Verify actual counts don't match declared counts
                        assertNotEquals(foundRepo.getLabelsCount(), foundRepo.getLabels().size());

                        // Verify language percentages sum to > 100
                        float totalPercentage = foundRepo.getLanguages().stream()
                                                         .map(Language::getPercentage)
                                                         .reduce(0f, Float::sum);
                        assertTrue(totalPercentage > 100);
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle repository metrics")
        void handleRepositoryMetrics(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("metricsTest");

            repo.setForkCount(100);
            repo.setStargazerCount(1000);
            repo.setWatcherCount(50);

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            // Update metrics
            repo.setForkCount(150);
            repo.setStargazerCount(1500);
            repo.setWatcherCount(75);

            asserter.execute(
                    () -> Panache.withTransaction(() -> repositoryRepository.persist(repo)));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    foundRepo -> {
                        assertEquals(150, foundRepo.getForkCount());
                        assertEquals(1500, foundRepo.getStargazerCount());
                        assertEquals(75, foundRepo.getWatcherCount());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should handle repository with topics")
        void repositoryWithTopics(UniAsserter asserter) {
            Repository repo = TestEntityCreators.createTestRepository("topicTest");

            Topic topic1 = new Topic();
            topic1.setName("spring-boot");

            Topic topic2 = new Topic();
            topic2.setName("java");

            repo.setTopics(List.of(topic1, topic2));
            repo.setTopicsCount(2);

            asserter.execute(() -> Panache.withTransaction(() ->
                            repositoryRepository.persist(repo)
                                                          ));

            asserter.assertThat(
                    () -> repositoryRepository.findById(repo.getId()),
                    foundRepo -> {
                        assertEquals(2, foundRepo.getTopicsCount());
                        assertEquals(2, foundRepo.getTopics().size());
                        assertTrue(foundRepo.getTopics().stream()
                                            .anyMatch(t -> t.getName().equals("spring-boot")));
                        assertTrue(foundRepo.getTopics().stream()
                                            .anyMatch(t -> t.getName().equals("java")));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }
}