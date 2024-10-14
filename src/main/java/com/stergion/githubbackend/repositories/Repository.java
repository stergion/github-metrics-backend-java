package com.stergion.githubbackend.repositories;

import com.stergion.githubbackend.utilityTypes.Github;
import com.stergion.githubbackend.utilityTypes.Label;
import com.stergion.githubbackend.utilityTypes.Language;
import com.stergion.githubbackend.utilityTypes.Topic;
import org.bson.types.ObjectId;

import java.util.List;


public class Repository {
    public ObjectId id;
    public String owner;
    public String name;
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
