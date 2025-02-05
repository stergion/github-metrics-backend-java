package com.stergion.githubbackend.infrastructure.persistence.postgres.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Label;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Language;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Topic;
import io.quarkus.logging.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.NaturalId;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Repositories",
        indexes = {@Index(name = "idx_repository_owner", columnList = "owner")}
)
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @NaturalId
    private String owner;

    @NotBlank
    @NaturalId
    private String name;

    @NotBlank
    @Column(unique = true)
    private String githubId;

    @NotNull
    @Column(unique = true)
    private URI githubUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "repositoryId")
    private List<Label> labels = new ArrayList<>();
    private int labelsCount;
    private String primaryLanguage;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "repositoryId")
    private List<Language> languages = new ArrayList<>();

    private int languagesCount;
    private int languagesSize;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "repositoryId")
//    @JoinTable(name = "RepositoryLabels")
    private List<Topic> topics = new ArrayList<>();

    private int topicsCount;
    private int forkCount;
    private int stargazerCount;
    private int watcherCount;


    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            Log.error(e.getClass()+ ": " + e.getMessage() + ". \nCause: " + e.getCause());
            return "{id: %s, owner: %s, name: %s}".formatted(id, owner, name);
        }
    }

    @Override
    public boolean equals(Object o) {

        return (o instanceof Repository repo)
                && owner.equals(repo.owner)
                && name.equals(repo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name);
    }

    /*
     *********************
     * Getters / Setters *
     *********************
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public URI getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(URI githubUrl) {
        this.githubUrl = githubUrl;
    }

    public List<Label> getLabels() {
        return List.copyOf(labels);
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public int getLabelsCount() {
        return labelsCount;
    }

    public void setLabelsCount(int labelsCount) {
        this.labelsCount = labelsCount;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public List<Language> getLanguages() {
        return List.copyOf(languages);
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }


    public int getLanguagesCount() {
        return languagesCount;
    }

    public void setLanguagesCount(int languagesCount) {
        this.languagesCount = languagesCount;
    }

    public int getLanguagesSize() {
        return languagesSize;
    }

    public void setLanguagesSize(int languagesSize) {
        this.languagesSize = languagesSize;
    }

    public List<Topic> getTopics() {
        return List.copyOf(topics);
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public int getTopicsCount() {
        return topicsCount;
    }

    public void setTopicsCount(int topicsCount) {
        this.topicsCount = topicsCount;
    }

    public int getForkCount() {
        return forkCount;
    }

    public void setForkCount(int forkCount) {
        this.forkCount = forkCount;
    }

    public int getStargazerCount() {
        return stargazerCount;
    }

    public void setStargazerCount(int stargazerCount) {
        this.stargazerCount = stargazerCount;
    }

    public int getWatcherCount() {
        return watcherCount;
    }

    public void setWatcherCount(int watcherCount) {
        this.watcherCount = watcherCount;
    }
}
