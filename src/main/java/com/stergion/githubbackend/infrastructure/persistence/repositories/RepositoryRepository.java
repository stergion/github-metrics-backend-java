package com.stergion.githubbackend.infrastructure.persistence.repositories;

import com.stergion.githubbackend.infrastructure.persistence.utilityTypes.NameWithOwner;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class RepositoryRepository implements PanacheMongoRepository<Repository> {
    public List<Repository> findById(List<ObjectId> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Document> criteria = ids.stream()
                                     .map(id -> new Document().append("_id", id))
                                     .toList();
        return list(new Document("$or", criteria));
    }


    public Repository findByNameAndOwner(String owner, String name) {
        return find("owner = ?1 and name = ?2", owner, name).firstResult();
    }

    public List<Repository> findByNameAndOwners(List<NameWithOwner> repos) {
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
}
