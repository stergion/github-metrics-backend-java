package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import jakarta.persistence.*;

import java.net.URI;
import java.util.UUID;

@Entity
@Table(name = "ClosingIssuesReferences")
public class ClosingIssuesReference {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String githubId;
    private URI githubUrl;

    public ClosingIssuesReference() {
    }

    public ClosingIssuesReference(String githubId, URI githubUrl) {
        this.githubId = githubId;
        this.githubUrl = githubUrl;
    }

    @Override
    public String toString() {
        return "{id: %s, githubId: %s, githubUrl: %s}".formatted(id, githubId, githubUrl);
    }
}
