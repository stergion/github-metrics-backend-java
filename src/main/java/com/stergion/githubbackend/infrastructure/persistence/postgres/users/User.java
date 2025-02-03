package com.stergion.githubbackend.infrastructure.persistence.postgres.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.annotations.NaturalId;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @NaturalId
    private String login;

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String githubId;

    @NotBlank
    @Column(unique = true)
    private URI githubUrl;

    @Email
    private String email;

    @PastOrPresent
    private LocalDateTime createdAt;

    @PastOrPresent
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "UserRepositories",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "repositoryId", referencedColumnName = "id")
    )
    private Set<Repository> repositories = new HashSet<>();

    private URI avatarUrl;
    private String bio;
    private String twitterHandle;
    private URI websiteUrl;

    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            return "{id: %s, login: %s, name: %s}".formatted(id, login, name);
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof User user) && login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Repository> getRepositories() {
        return Set.copyOf(repositories);
    }

    public void setRepositories(Set<Repository> repositories) {
        this.repositories = repositories;
    }

    public URI getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(URI avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public URI getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(URI websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
