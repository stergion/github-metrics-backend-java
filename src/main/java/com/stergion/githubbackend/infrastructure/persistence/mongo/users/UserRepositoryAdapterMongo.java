package com.stergion.githubbackend.infrastructure.persistence.mongo.users;

import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.domain.users.UserRepository;
import io.smallrye.mutiny.Uni;

import java.util.List;

public class UserRepositoryAdapterMongo implements UserRepository {
    private final UserRepositoryMongo repository;

    private final UserMapper mapper;

    public UserRepositoryAdapterMongo(UserRepositoryMongo userRepository, UserMapper userMapper) {
        this.repository = userRepository;
        this.mapper = userMapper;
    }

    @Override
    public Uni<User> persist(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        return repository.persist(userEntity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Void> persist(List<User> users) {
        List<UserEntity> userEntities = users.stream()
                                             .map(mapper::toEntity)
                                             .toList();

        return repository.persist(userEntities);
    }

    @Override
    public Uni<User> update(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        return repository.update(userEntity)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<Void> update(List<User> users) {
        List<UserEntity> userEntities = users.stream().map(mapper::toEntity).toList();
        return repository.update(userEntities);
    }

    @Override
    public Uni<Void> delete(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        return repository.delete(userEntity);
    }

    @Override
    public Uni<Void> deleteByLogin(String login) {
        return repository.deleteByLogin(login)
                         .replaceWithVoid();
    }

    @Override
    public Uni<User> findByLogin(String login) {
        return repository.findByLogin(login)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<List<User>> findByEmail(String email) {
        return repository.findByEmail(email)
                         .map(mapper::toDomain);
    }

    @Override
    public Uni<User> findByGitHubId(String id) {
        return repository.findByGitHubId(id)
                         .map(mapper::toDomain);
    }
}
