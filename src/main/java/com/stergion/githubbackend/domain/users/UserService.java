package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.repositories.RepositoryService;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.UserClient;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.UserEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.users.UserRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class UserService {
    @Inject
    UserClient client;

    @Inject
    UserMapper mapper;

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
        var userEntity = mapper.toEntity(user);
        repository.persist(userEntity);
        return mapper.toDomain(userEntity);
    }

    private User updateUser(User user) {
        var userEntity = mapper.toEntity(user);
        repository.update(userEntity);
        return mapper.toDomain(userEntity);
    }

    public User fetchAndCreateUser(String login) {
        if (check(login)) {
            throw new UserAlreadyExistsException(login);
        }
//      Create User only if not found
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
        UserEntity user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return mapper.toDomain(user);
    }

    public ObjectId getUserId(@NotNull String login) {
        UserEntity user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return user.id;
    }

    public boolean check(String login) {
        return repository.findByLogin(login) != null;
    }

    public User updateRepositories(User user, List<Repository> repos) {
        if (repos == null || repos.isEmpty()) {
            Log.warn("Received null repository list for user: " + user.login());
            return user;
        }

        List<ObjectId> ids = repos.stream().map(Repository::id).toList();

        return updateRepositoriesFromIds(user, ids);
    }

    public User updateRepositoriesFromIds(User user, List<ObjectId> repoIds){
        if (repoIds == null || repoIds.isEmpty()) {
            return user;  // No changes needed
        }

        UserEntity userEntity = mapper.toEntity(user);
        Set<ObjectId> uniqueIds = new HashSet<>(userEntity.repositories);  // Start with existing repos
        uniqueIds.addAll(repoIds);  // Add new ones

        if (uniqueIds.size() == userEntity.repositories.size()) {
            return user;  // No new unique repos were added
        }

        userEntity.repositories = new ArrayList<>(uniqueIds);
        repository.update(userEntity);

        return mapper.toDomain(userEntity);
    }

    public List<Repository> getUserRepositories(String login) {
        UserEntity user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }

        List<ObjectId> repoIds = user.repositories;
        return repositoryService.getRepositories(repoIds);
    }

    public void deleteUser(String login) {
        repository.deleteByLogin(login);
    }
}
