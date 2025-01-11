package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.domain.repositories.RepositoryDTO;
import com.stergion.githubbackend.infrastructure.external.githubservice.service.UserClient;
import com.stergion.githubbackend.infrastructure.persistence.users.User;
import com.stergion.githubbackend.infrastructure.persistence.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    private UserDTO fetchUser(String login) {
        UserDTO user = client.getUserInfo(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return user;
    }

    private UserDTO createUser(UserDTO userDTO) {
        var user = mapper.toEntity(userDTO);
        repository.persist(user);
        return mapper.toDTO(user);
    }

    public UserDTO fetchAndCreateUser(String login) {
        if (check(login)) {
            throw new UserAlreadyExistsException(login);
        }
//      Create User only if not found
        UserDTO userDTO = fetchUser(login);

        return createUser(userDTO);
    }

    public UserDTO getUser(String login) {
        User user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return mapper.toDTO(user);
    }

    public ObjectId getUserId(@NotNull String login) {
        User user = repository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(login);
        }
        return user.id;
    }

    public boolean check(String login) {
        return repository.findByLogin(login) != null;
    }

    public UserDTO updateRepositories(UserDTO userDTO, List<RepositoryDTO> repos) {
        List<ObjectId> ids = repos.stream().map(RepositoryDTO::id).toList();
        return updateRepositoriesFromIds(userDTO, ids);
    }

    public UserDTO updateRepositoriesFromIds(UserDTO userDTO, List<ObjectId> repoIds){
        if (repoIds == null || repoIds.isEmpty()) {
            return userDTO;  // No changes needed
        }

        User user = mapper.toEntity(userDTO);
        Set<ObjectId> uniqueIds = new HashSet<>(user.repositories);  // Start with existing repos
        uniqueIds.addAll(repoIds);  // Add new ones

        if (uniqueIds.size() == user.repositories.size()) {
            return userDTO;  // No new unique repos were added
        }

        user.repositories = new ArrayList<>(uniqueIds);
        repository.update(user);

        return mapper.toDTO(user);
    }
}
