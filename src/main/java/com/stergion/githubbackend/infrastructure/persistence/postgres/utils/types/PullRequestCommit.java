package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import jakarta.persistence.*;

import java.net.URI;
import java.util.UUID;

@Entity
@Table(name = "PullRequestCommits")
public class PullRequestCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String githubId;

    private URI githubUrl;

    private int additions;

    private int deletions;

    private int changedFiles;


    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            return "{id: %s, githubId: %s }".formatted(id, githubId);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public int getChangedFiles() {
        return changedFiles;
    }

    public void setChangedFiles(int changedFiles) {
        this.changedFiles = changedFiles;
    }
}
