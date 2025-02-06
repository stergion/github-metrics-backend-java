package com.stergion.githubbackend.infrastructure.persistence.postgres.users;

import com.stergion.githubbackend.infrastructure.persistence.postgres.TestEntityCreators;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.RepositoryRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Inject
    UserRepository userRepository;


    @Inject
    RepositoryRepository repositoryRepository;

    @BeforeEach
    @RunOnVertxContext
    void setUp(UniAsserter asserter) {
        asserter.execute(() -> Panache.withTransaction(() -> userRepository.deleteAll()));
        asserter.execute(() -> Panache.withTransaction(() -> repositoryRepository.deleteAll()));

        asserter.surroundWith(u -> Panache.withSession(() -> u));
    }

    @Nested
    @DisplayName("Basic CRUD Operations")
    class CrudOperations {

        @Test
        @RunOnVertxContext
        @DisplayName("Should create a new user successfully")
        void createUser(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();

            asserter.assertThat(
                    () -> Panache.withTransaction(() -> userRepository.persist(user)),
                    persistedUser -> {
                        assertNotNull(persistedUser.getId());
                        assertEquals(user.getLogin(), persistedUser.getLogin());
                        assertEquals(user.getName(), persistedUser.getName());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find user by ID")
        void findUserById(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();

            asserter.execute(
                    () -> Panache.withTransaction(() -> userRepository.persist(user)));

            asserter.assertThat(
                    () -> userRepository.findById(user.getId()),
                    foundUser -> {
                        assertNotNull(foundUser);
                        assertEquals(user.getLogin(), foundUser.getLogin());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should update existing user")
        void updateUser(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();
            String updatedName = "updatedName";

            asserter.execute(
                    () -> Panache.withTransaction(() -> userRepository.persist(user)));


            asserter.execute(
                    () -> {
                        user.setName(updatedName);
                        return Panache.withTransaction(() -> userRepository.persist(user));
                    });

            asserter.assertThat(
                    () -> userRepository.findById(user.getId()),
                    updatedUser -> {
                        assertNotEquals(updatedName, updatedUser.getLogin());
                        assertEquals(user.getName(), updatedUser.getName());
                        assertEquals(user.getId(), updatedUser.getId());
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete existing user")
        void deleteUser(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();

            asserter.execute(
                    () -> Panache.withTransaction(() -> userRepository.persist(user)));
            asserter.execute(
                    () -> Panache.withTransaction(() -> userRepository.deleteById(user.getId())
                                                                      .chain(() -> userRepository.flush())));

            asserter.assertThat(
                    () -> userRepository.findById(user.getId()),
                    Assertions::assertNull);

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Custom Finder Methods")
    class CustomFinderTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should find user by login")
        void findByLogin(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();

            asserter.execute(() -> Panache.withTransaction(() -> userRepository.persist(user)));

            asserter.assertThat(
                    () -> userRepository.findByLogin(user.getLogin()),
                    foundUser -> {
                        assertNotNull(foundUser);
                        assertEquals(user.getLogin(), foundUser.getLogin());
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> userRepository.findByLogin("nonexistent"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find users by email")
        void findByEmail(UniAsserter asserter) {
            User user1 = TestEntityCreators.createUser("tUser1");
            user1.setEmail("same@email.com");

            User user2 = TestEntityCreators.createUser("tUser2");
            user2.setEmail("same@email.com");

            asserter.execute(() -> Panache.withTransaction(() ->
                            userRepository.persist(user1)
                                          .chain(() -> userRepository.persist(user2))
                                                          ));

            asserter.assertThat(
                    () -> userRepository.findByEmail("same@email.com"),
                    users -> {
                        assertEquals(2, users.size());
                        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("tUser1")));
                        assertTrue(users.stream().anyMatch(u -> u.getLogin().equals("tUser2")));
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> userRepository.findByEmail("nonexistent@email.com"),
                    users -> assertTrue(users.isEmpty())
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should find user by GitHub ID")
        void findByGithubId(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();

            asserter.execute(() -> Panache.withTransaction(() -> userRepository.persist(user)));

            asserter.assertThat(
                    () -> userRepository.findByGitHubId(user.getGithubId()),
                    foundUser -> {
                        assertNotNull(foundUser);
                        assertEquals(user.getGithubId(), foundUser.getGithubId());
                    }
                               );

            // Test not found case
            asserter.assertThat(
                    () -> userRepository.findByGitHubId("nonexistent"),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }

    @Nested
    @DisplayName("Custom Delete Methods")
    class CustomDeleteTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should delete user by login")
        void deleteByLogin(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();

            asserter.execute(() -> Panache.withTransaction(() -> userRepository.persist(user)));

            asserter.execute(() -> Panache.withTransaction(() ->
                    userRepository.deleteByLogin(user.getLogin())));

            asserter.assertThat(
                    () -> userRepository.findByLogin(user.getLogin()),
                    Assertions::assertNull
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }


    @Nested
    @DisplayName("Repository Association Tests")
    class RepositoryAssociationTests {

        @Test
        @RunOnVertxContext
        @DisplayName("Should add repositories to user")
        void addRepositoriesToUser(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();
            Set<Repository> repositories = Set.of(
                    TestEntityCreators.createRepository("repo1"),
                    TestEntityCreators.createRepository("repo2")
                                                 );

            asserter.execute(() -> Panache.withTransaction(
                    () -> repositoryRepository.persist(repositories)));

            repositories.forEach(i -> System.out.println(i.getId()));

            asserter.execute(() -> Panache.withTransaction(() -> {
                user.setRepositories(new HashSet<>(repositories));
                return userRepository.persist(user);
            }));

            asserter.assertThat(
                    () -> userRepository.findById(user.getId()),
                    foundUser -> {
                        Set<Repository> userRepos = foundUser.getRepositories();
                        assertEquals(2, userRepos.size());
                        assertTrue(userRepos.stream()
                                            .anyMatch(r -> r.getName().equals("repo1")));
                        assertTrue(userRepos.stream()
                                            .anyMatch(r -> r.getName().equals("repo2")));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }

        @Test
        @RunOnVertxContext
        @DisplayName("Should remove repository from user")
        void removeRepositoryFromUser(UniAsserter asserter) {
            User user = TestEntityCreators.createUser();
            Set<Repository> repositories = new HashSet<>(Set.of(
                    TestEntityCreators.createRepository("repo1"),
                    TestEntityCreators.createRepository("repo2")
                                                               ));

            asserter.execute(() -> Panache.withTransaction(
                    () -> repositoryRepository.persist(repositories)));

            // First persist user with repositories
            asserter.execute(() -> Panache.withTransaction(() -> {
                user.setRepositories(repositories);
                return userRepository.persist(user);
            }));

            asserter.assertThat(
                    () -> userRepository.findById(user.getId()),
                    foundUser -> {
                        Set<Repository> userRepos = foundUser.getRepositories();
                        assertEquals(2, userRepos.size());
                    }
                               );

            // Remove one repository
            asserter.execute(() -> Panache.withTransaction(() -> {
                repositories.removeIf(r -> r.getName().equals("repo1"));
                user.setRepositories(repositories);
                return userRepository.persist(user);
            }));

            // Verify
            asserter.assertThat(
                    () -> userRepository.findById(user.getId()),
                    foundUser -> {
                        Set<Repository> userRepos = foundUser.getRepositories();
                        assertEquals(1, userRepos.size());
                        assertTrue(userRepos.stream()
                                            .anyMatch(r -> r.getName().equals("repo2")));
                    }
                               );

            asserter.surroundWith(u -> Panache.withSession(() -> u));
        }
    }
}