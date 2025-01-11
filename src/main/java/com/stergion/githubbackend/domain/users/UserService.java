package com.stergion.githubbackend.domain.users;

import com.stergion.githubbackend.infrastructure.external.githubservice.service.UserClient;
import com.stergion.githubbackend.infrastructure.persistence.users.User;
import com.stergion.githubbackend.infrastructure.persistence.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

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

}
