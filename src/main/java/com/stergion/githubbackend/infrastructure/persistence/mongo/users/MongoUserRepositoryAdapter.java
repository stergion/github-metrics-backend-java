package com.stergion.githubbackend.infrastructure.persistence.mongo.users;

import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.domain.users.UserRepository;

import java.util.List;

public class MongoUserRepositoryAdapter implements UserRepository {
    private final MongoUserRepository repository;

    private final UserMapper mapper;

    public MongoUserRepositoryAdapter(MongoUserRepository userRepository, UserMapper userMapper) {
        this.repository = userRepository;
        this.mapper = userMapper;
    }

    @Override
    public User persist(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        repository.persist(userEntity);
        return mapper.toDomain(userEntity);
    }

    @Override
    public List<User> persist(List<User> users) {
        List<UserEntity> userEntities = users.stream().map(mapper::toEntity).toList();
        repository.persist(userEntities);
        return userEntities.stream()
                           .map(mapper::toDomain)
                           .toList();
    }

    @Override
    public User update(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        repository.update(userEntity);
        return mapper.toDomain(userEntity);
    }

    @Override
    public List<User> update(List<User> users) {
        List<UserEntity> userEntities = users.stream().map(mapper::toEntity).toList();
        repository.update(userEntities);
        return userEntities.stream()
                           .map(mapper::toDomain)
                           .toList();
    }

    @Override
    public void delete(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        repository.delete(userEntity);
    }

    @Override
    public void deleteByLogin(String login) {
        repository.deleteByLogin(login);
    }

    @Override
    public User findByLogin(String login) {
        UserEntity userEntity = repository.findByLogin(login);
        return mapper.toDomain(userEntity);
    }

    @Override
    public List<User> findByEmail(String email) {
        List<UserEntity> userEntities = repository.findByEmail(email);

        return userEntities.stream()
                           .map(mapper::toDomain)
                           .toList();
    }

    @Override
    public User findByGitHubId(String id) {
        var userEntity = repository.findByGitHubId(id);
        return mapper.toDomain(userEntity);
    }
}
