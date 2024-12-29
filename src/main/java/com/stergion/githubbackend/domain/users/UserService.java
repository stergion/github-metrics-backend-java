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

    private void createUser(UserDTO userDTO) {
        repository.persist(mapper.toEntity(userDTO));
    }

    public void fetchAndCreateUser(String login) {
        try {
            getUser(login);
        } catch (UserNotFoundException e) {
//            Create User only if not found
            UserDTO userDTO = fetchUser(login);
            createUser(userDTO);
        }
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
}
