package com.stergion.githubbackend.domain.utils;

import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.enterprise.context.RequestScoped;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequestScoped
public class RepositoryIdCache {
    final Map<NameWithOwner, ObjectId> repoIdCache;

    public RepositoryIdCache() {
        this.repoIdCache = new ConcurrentHashMap<>(1000);
    }

    public RepositoryIdCache(int initialCapacity) {
        this.repoIdCache = new ConcurrentHashMap<>(initialCapacity);
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
