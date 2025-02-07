package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "PullRequestReviewComments")
public class PullRequestReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String login;
    private String githubId;
    private String githubUrl;
    private String body;

    public PullRequestReviewComment() {
    }

    public PullRequestReviewComment(String login, String githubId, String githubUrl, String body) {
        this.login = login;
        this.githubId = githubId;
        this.githubUrl = githubUrl;
        this.body = body;
    }

    @Override
    public String toString() {
        return "{ id: %s, login: %s, githubId: %s, githubUrl: %s, body: %s }".formatted(id, login,
                githubId, githubUrl, body);
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
