package com.stergion.githubbackend.domain.repositories;


import com.stergion.githubbackend.domain.utils.types.NameWithOwner;

import java.util.List;

public interface RepositoryRepository {
    void persist(Repository repository);

    void persist(List<Repository> repository);

    void delete(Repository repository);

    void deleteByOwner(String owner);

    void update(Repository repository);

    void update(List<Repository> repositories);

    List<Repository> findById(List<String> ids);

    List<Repository> findByOwner(String owner);

    Repository findByNameAndOwner(String owner, String name);

    List<Repository> findByNameAndOwner(List<NameWithOwner> nameWithOwners);

    Repository findByGitHubId(String id);
}
