package com.stergion.githubbackend.domain.utils;

import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequestScoped
public class RepositoryIdCache {
    @ConfigProperty(name = "repository.cache.size", defaultValue = "1000")
    int capacity;

    private Map<NameWithOwner, ObjectId> repoIdCache;

    @PostConstruct
    void init() {
        this.repoIdCache = new ConcurrentHashMap<>(capacity);
    }

    public ObjectId get(NameWithOwner nameWithOwner) {
        return repoIdCache.get(nameWithOwner);
    }

    public Map<NameWithOwner, ObjectId> getAll(Collection<NameWithOwner> repos) {
        return repos.stream()
                    .filter(repoIdCache::containsKey)
                    .collect(Collectors.toMap(
                            repo -> repo,
                            repoIdCache::get
                                             ));
    }

    public void put(NameWithOwner nameWithOwner, ObjectId id) {
        repoIdCache.put(nameWithOwner, id);
    }

    public void putAll(Map<NameWithOwner, ObjectId> entries) {
        repoIdCache.putAll(entries);
    }
}
