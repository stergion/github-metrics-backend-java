package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.domain.utils.types.Github;
import com.stergion.githubbackend.domain.utils.types.Label;
import com.stergion.githubbackend.domain.utils.types.Language;
import com.stergion.githubbackend.domain.utils.types.Topic;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.RepositoryGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.helpers.RepositoryOwner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RepositoryBuilder {
    private String id = "test-repo-id";
    private String name = "test-repo";
    private String owner = "test-owner";
    private int stargazerCount = 100;
    private int forkCount = 50;
    private int watcherCount = 25;
    private String primaryLanguage = "Java";
    private List<Language> languages = new ArrayList<>();
    private List<Label> labels = new ArrayList<>();
    private List<Topic> topics = new ArrayList<>();

    public RepositoryBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public RepositoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RepositoryBuilder withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public RepositoryBuilder withStargazerCount(int count) {
        this.stargazerCount = count;
        return this;
    }

    public RepositoryBuilder withForkCount(int count) {
        this.forkCount = count;
        return this;
    }

    public RepositoryBuilder withWatcherCount(int count) {
        this.watcherCount = count;
        return this;
    }

    public RepositoryBuilder withPrimaryLanguage(String language) {
        this.primaryLanguage = language;
        return this;
    }

    public RepositoryBuilder withLanguages(List<Language> languages) {
        this.languages = new ArrayList<>(languages);
        return this;
    }

    public RepositoryBuilder withLabels(List<Label> labels) {
        this.labels = new ArrayList<>(labels);
        return this;
    }

    public RepositoryBuilder withTopics(List<Topic> topics) {
        this.topics = new ArrayList<>(topics);
        return this;
    }

    public RepositoryGH build() {
        try {
            URI url = new URI("https://github.com/" + owner + "/" + name);

            List<RepositoryGH.LanguageEdge> languageEdges = languages.stream()
                                                                     .map(lang -> new RepositoryGH.LanguageEdge(
                                                                             lang.size(),
                                                                             new RepositoryGH.LanguageInfo(
                                                                                     lang.name())
                                                                     ))
                                                                     .toList();

            return new RepositoryGH(
                    id,
                    name,
                    url,
                    new RepositoryOwner(owner),
                    stargazerCount,
                    forkCount,
                    primaryLanguage != null ? new RepositoryGH.LanguageInfo(primaryLanguage) : null,
                    new RepositoryGH.LanguagesConnection(
                            languages.size(),
                            languages.stream().mapToInt(Language::size).sum(),
                            languageEdges
                    ),
                    null,  // labels connection - not needed for most tests
                    null,  // repository topics - not needed for most tests
                    new RepositoryGH.WatchersConnection(watcherCount)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to build RepositoryGH", e);
        }
    }

    public Repository buildDTO() {
        try {
            return new Repository(
                    null,
                    owner,
                    name,
                    new Github(id, new URI("https://github.com/" + owner + "/" + name)),
                    labels,
                    labels.size(),
                    primaryLanguage,
                    languages,
                    languages.size(),
                    languages.stream().mapToInt(Language::size).sum(),
                    topics,
                    topics.size(),
                    forkCount,
                    stargazerCount,
                    watcherCount
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to build Repository", e);
        }
    }

    public List<RepositoryGH> buildList(int count) {
        List<RepositoryGH> repositories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repositories.add(new RepositoryBuilder()
                    .withId(id + "-" + i)
                    .withName(name + "-" + i)
                    .withOwner(owner)
                    .build());
        }
        return repositories;
    }

    public List<Repository> buildDTOList(int count) {
        List<Repository> repositories = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repositories.add(new RepositoryBuilder()
                    .withId(id + "-" + i)
                    .withName(name + "-" + i)
                    .withOwner(owner)
                    .buildDTO());
        }
        return repositories;
    }
}