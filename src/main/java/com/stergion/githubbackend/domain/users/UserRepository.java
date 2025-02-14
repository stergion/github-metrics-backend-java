package com.stergion.githubbackend.domain.users;

import java.util.List;

public interface UserRepository {
    User persist(User user);

    List<User> persist(List<User> users);

    User update(User user);

    List<User> update(List<User> users);

    void delete(User user);

    void deleteByLogin(String login);

    User findByLogin(String login);

    List<User> findByEmail(String email);

    User findByGitHubId(String id);
}
