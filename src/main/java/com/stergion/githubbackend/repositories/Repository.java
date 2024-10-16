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
}
