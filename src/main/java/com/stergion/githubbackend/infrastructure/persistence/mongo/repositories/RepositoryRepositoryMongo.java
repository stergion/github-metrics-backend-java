package com.stergion.githubbackend.infrastructure.persistence.mongo.repositories;

import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RepositoryRepositoryMongo implements PanacheMongoRepository<RepositoryEntity> {
    public List<RepositoryEntity> findById(List<ObjectId> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Document> criteria = ids.stream()
                                     .map(id -> new Document().append("_id", id))
                                     .toList();
        return list(new Document("$or", criteria));
    }

    public RepositoryEntity findByGitHubId(String gitHubId) {
        return find("github.id", gitHubId).firstResult();
    }

    public List<RepositoryEntity> findByOwner(String owner) {
        return find("owner", owner).list();
    }

    public RepositoryEntity findByNameAndOwner(String owner, String name) {
        return find("owner = ?1 and name = ?2", owner, name).firstResult();
    }

    public List<RepositoryEntity> findByNameAndOwners(List<NameWithOwner> repos) {
        if (repos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Document> criteria = repos.stream()
                                       .map(repo -> new Document()
                                               .append("owner", repo.owner())
                                               .append("name", repo.name()))
                                       .toList();

        return find(new Document("$or", criteria)).list();
    }

    public void deleteByOwner(String owner) {
        delete("owner", owner);
    }

    public void save(RepositoryEntity repository) {
        var found = findByNameAndOwner(repository.owner, repository.name);
        if (found != null) {
            repository.id = found.id;
            update(repository);
        } else {
            persist(repository);
        }
    }

    public void save(List<RepositoryEntity> repositories) {
        if (repositories.isEmpty()) {
            return;
        }

        List<NameWithOwner> repoIds = repositories.stream()
                                                          .map(r -> new NameWithOwner(r.owner,
                                                                  r.name))
                                                          .toList();
        List<RepositoryEntity> existingRepos = findByNameAndOwners(repoIds);

        Map<String, RepositoryEntity> existingRepoMap = existingRepos.stream()
                                                                     .collect(Collectors.toMap(
                                                                       r -> r.owner + "/" + r.name,
                                                                       repo -> repo
                                                                                        ));
        repositories.forEach(newRepo -> {
            String key = newRepo.owner + "/" + newRepo.name;
            RepositoryEntity existingRepo = existingRepoMap.get(key);

            if (existingRepo != null) {
                // Update existing repository
                newRepo.id = existingRepo.id;
                update(newRepo);
            } else {
                // Insert new repository
                persist(newRepo);
            }
        });
    }
}
