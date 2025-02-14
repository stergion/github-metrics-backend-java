package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.UserClient;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class UserService {
    @Inject
    UserClient client;

    @Inject
    UserRepository repository;

    @Inject
    RepositoryService repositoryService;

    private User fetchUser(String login) {
        User user = client.getUserInfo(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return user;
    }

    private User createUser(User user) {
        return repository.persist(user);
    }

    private User updateUser(User user) {
        return repository.update(user);
    }

    public User fetchAndCreateUser(String login) {
        if (check(login)) {
            throw new UserAlreadyExistsException(login);
        }
        // Create User only if not found
        User user = fetchUser(login);

        return createUser(user);
    }

    public User fetchAndUpdateUser(@NotBlank String login) {
        if (!check(login)) {
            throw new UserNotFoundException(login);
        }
        User user = fetchUser(login);
        return updateUser(user);
    }

    public User getUser(String login) {
        User user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return user;
    }

    public String getUserId(@NotNull String login) {
        User user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return user.id();
    }

    public boolean check(String login) {
        return repository.findByLogin(login) != null;
    }

    public User updateRepositories(User user, List<Repository> repos) {
        if (repos == null || repos.isEmpty()) {
            Log.warn("Received null repository list for user: " + user.login());
            return user;
        }

        List<String> ids = repos.stream().map(Repository::id).toList();

        return updateRepositoriesFromIds(user, ids);
    }

    public User updateRepositoriesFromIds(User user, List<String> repoIds) {
        if (repoIds == null || repoIds.isEmpty()) {
            return user;  // No changes needed
        }

        Set<String> uniqueIds = new HashSet<>(
                user.repositories());  // Start with existing repos
        uniqueIds.addAll(repoIds);  // Add new ones

        if (uniqueIds.size() == user.repositories().size()) {
            return user;  // No new unique repos were added
        }

        user.repositories().clear();
        user.repositories().addAll(uniqueIds);

        return repository.update(user);
    }

    public List<Repository> getUserRepositories(String login) {
        User user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }

        List<String> repoIds = user.repositories();
        return repositoryService.getRepositories(repoIds);
    }

    public void deleteUser(String login) {
        repository.deleteByLogin(login);
    }
}
