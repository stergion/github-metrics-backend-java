package com.stergion.githubbackend.domain.repositories;

public class RepositoryNotFoundException extends RuntimeException {
    String owner;
    String name;

    public RepositoryNotFoundException(String owner, String name) {
        super("Repository not found: '" + owner + "/" + name + "'");
        this.owner = owner;
        this.name = name;
    }
}
