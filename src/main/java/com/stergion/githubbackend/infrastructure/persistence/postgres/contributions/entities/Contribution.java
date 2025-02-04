package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.UUID;

@Entity
@Table(
        name = "Contributions",
        indexes = {
                @Index(name = "idx_contribution_user", columnList = "userId"),
                @Index(name = "idx_contribution_repository", columnList = "repositoryId"),
                @Index(name = "idx_contribution_user_repo", columnList = "userId,repositoryId")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "repositoryId", nullable = false)
    private Repository repository;

    @NotBlank
    @Column(unique = true)
    private String githubId;


    @NotNull
    @Column(unique = true)
    private URI githubUrl;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(
            Repository repository) {
        this.repository = repository;
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
