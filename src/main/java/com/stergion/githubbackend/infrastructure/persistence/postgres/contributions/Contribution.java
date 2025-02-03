package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions;

import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import com.stergion.githubbackend.infrastructure.persistence.postgres.users.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(indexes = {
        @Index(columnList = "id"),
        @Index(columnList = "user"),
        @Index(columnList = "repository"),
        @Index(columnList = "user, repository"),
        @Index(columnList = "githubId"),
})
public abstract class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    private Repository repository;

    private String githubId;
    private String githubUrl;

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

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }
}
