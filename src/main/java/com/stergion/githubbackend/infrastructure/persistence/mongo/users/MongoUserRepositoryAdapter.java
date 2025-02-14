package com.stergion.githubbackend.infrastructure.persistence.mongo.users;

import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.domain.users.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

public class MongoUserRepositoryAdapter implements UserRepository {
    private final MongoUserRepository repository;

    private final UserMapper mapper;

    public MongoUserRepositoryAdapter(MongoUserRepository userRepository, UserMapper userMapper) {
        this.repository = userRepository;
        this.mapper = userMapper;
    }

    @Override
    public void persist(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        repository.persist(userEntity);
    }

    @Override
    public void persist(List<User> users) {
        Stream<UserEntity> userEntitiesStream = users.stream().map(mapper::toEntity);
        repository.persist(userEntitiesStream);
    }

    @Override
    public  void update(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        repository.update(userEntity);
    }

    @Override
    public void update(List<User> users) {
        Stream<UserEntity> userEntitiesStream = users.stream().map(mapper::toEntity);
        repository.update(userEntitiesStream);
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
