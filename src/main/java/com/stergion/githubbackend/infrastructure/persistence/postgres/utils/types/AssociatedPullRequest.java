package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import jakarta.persistence.*;

import java.net.URI;
import java.util.UUID;

@Entity
@Table(name = "AssociatedPullRequests")
public class AssociatedPullRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String githubId;
    private URI githubUrl;

    public AssociatedPullRequest() {
    }

    public AssociatedPullRequest(String githubId, URI githubUrl) {
        this.githubId = githubId;
        this.githubUrl = githubUrl;
    }

    @Override
    public String toString() {
        return "{id: %s, githubId: %s, githubUrl: %s}".formatted(id, githubId, githubUrl);
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
}
