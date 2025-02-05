package com.stergion.githubbackend.infrastructure.persistence.postgres;

import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.User;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TestEntityCreators {

    public static User createTestUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setName("Test User " + login);
        user.setGithubId("github-" + login);
        user.setGithubUrl(URI.create("https://github.com/" + login));
        user.setEmail(login + "@example.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    public static User createTestUser() {
        return createTestUser("testUser");
    }

    public static Repository createTestRepository(String name, String owner) {
        Repository repo = new Repository();
        repo.setName(name);
        repo.setOwner(owner);
        repo.setGithubId(owner + "-" + name + "-id");
        repo.setGithubUrl(URI.create("https://github.com/" + owner + "/" + name));
        return repo;
    }

    public static Repository createTestRepository(String name) {
        return createTestRepository(name, "testOwner");
    }

    public static List<Repository> createTestRepositories(int count) {
        List<Repository> repositories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repositories.add(createTestRepository("repo" + i));
        }
        return repositories;
    }

    public static List<User> createTestUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(createTestUser("testUser" + i));
        }
        return users;
    }
}
