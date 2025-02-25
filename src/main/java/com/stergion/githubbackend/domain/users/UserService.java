package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.UserClient;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
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

    private Uni<User> fetchUser(String login) {
        return Uni.createFrom().item(() -> client.getUserInfo(login))
                  .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                  .onItem().ifNull().failWith(new UserNotFoundException(login))
                  .onFailure().retry().atMost(3);
    }

    private Uni<User> createUser(User user) {
        return repository.persist(user);
    }

    private Uni<User> updateUser(User user) {
        return repository.update(user);
    }

    public Uni<User> fetchAndCreateUser(String login) {
        return repository.findByLogin(login)
                         .onItem().ifNotNull()
                         .failWith(new UserAlreadyExistsException(login))
                         .onItem().ifNull()
                         .switchTo(() -> fetchUser(login))
                         .chain(repository::persist);


    }

    public Uni<User> fetchAndUpdateUser(@NotBlank String login) {
        return repository.findByLogin(login)
                         .onItem().ifNull()
                         .failWith(new UserNotFoundException(login))
                         .onItem().ifNotNull()
                         .transformToUni(user -> fetchUser(user.login()))
                         .chain(repository::update);

    }

    public Uni<User> getUser(String login) {
        return repository.findByLogin(login)
                         .onItem().ifNull()
                         .failWith(new UserNotFoundException(login));
    }

    public Uni<String> getUserId(@NotNull String login) {
        return repository.findByLogin(login)
                         .onItem().ifNull()
                         .failWith(new UserNotFoundException(login))
                         .map(User::id);
    }

    public Uni<User> updateRepositories(User user, List<Repository> repos) {
        if (repos == null || repos.isEmpty()) {
            Log.warn("Received null repository list for user: " + user.login());
            return Uni.createFrom().item(user);
        }

        List<String> ids = repos.stream().map(Repository::id).toList();

        return updateRepositoriesFromIds(user, ids);
    }

    public Uni<User> updateRepositoriesFromIds(User user, List<String> repoIds) {
        if (repoIds == null || repoIds.isEmpty()) {
            return Uni.createFrom().item(user);  // No changes needed
        }

        Set<String> uniqueIds = new HashSet<>(user.repositories());  // Start with existing repos
        uniqueIds.addAll(repoIds);  // Add new ones

        if (uniqueIds.size() == user.repositories().size()) {
            return Uni.createFrom().item(user);  // No new unique repos were added
        }

        user.repositories().clear();
        user.repositories().addAll(uniqueIds);

        return repository.update(user);
    }

    public Uni<List<Repository>> getUserRepositories(String login) {
        return repository.findByLogin(login)
                         .onItem().ifNull()
                         .failWith(new UserNotFoundException(login))
                         .onItem().ifNotNull()
                         .transform(user -> repositoryService.getRepositories(user.repositories()));
    }

    public Uni<Void> deleteUser(String login) {
        return repository.deleteByLogin(login);
    }
}
