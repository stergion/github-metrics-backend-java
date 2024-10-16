package com.stergion.githubbackend.repositories;

import com.stergion.githubbackend.utilityTypes.Github;
import com.stergion.githubbackend.utilityTypes.Label;
import com.stergion.githubbackend.utilityTypes.Language;
import com.stergion.githubbackend.utilityTypes.Topic;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(collection = "repositories")
public class Repository {
    @NotNull
    public ObjectId id;

    @NotBlank
    public String owner;

    @NotBlank
    public String name;

    @NotNull
    public Github github;


    public List<Label> labels;
    public int labelsCount;
    public String primaryLanguage;
    public List<Language> languages;
    public int languagesCount;
    public int languagesSize;
    public List<Topic> topics;
    public int topicsCount;
    public int forkCount;
    public int stargazersCount;
    public int watchersCount;

    @Override
    public String toString() {
        return """
                {
                    id: %s,
                    owner: %s,
                    name: %s,
                    github: %s,
                    labels: %s,
                    labelsCount: %s,
                    primaryLanguage: %s,
                    languages: %s,
                    languagesCount: %s,
                    languagesSize: %s,
                    topics: %s,
                    topicsCount: %s,
                    forkCount: %s,
                    stargazersCount: %s,
                    watchersCount: %s
                }
                """.formatted(
                id,
                owner,
                name,
                github,
                labels,
                labelsCount,
                primaryLanguage,
                languages,
                languagesCount,
                languagesSize,
                topics,
                topicsCount,
                forkCount,
                stargazersCount,
                watchersCount
        );
    }

    @Override
    public final boolean equals(Object o) {
        return (o instanceof Repository r)
                && owner.equals(r.owner)
                && name.equals(r.name);
    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
