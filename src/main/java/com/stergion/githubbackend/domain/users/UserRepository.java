package com.stergion.githubbackend.domain.users;

import java.util.List;

public interface UserRepository {
    void persist(User user);

    void persist(List<User> users);

    void update(User user);

    void update(List<User> users);

    void delete(User user);

    void deleteByLogin(String login);

    User findByLogin(String login);

    List<User> findByEmail(String email);

    User findByGitHubId(String id);
}
