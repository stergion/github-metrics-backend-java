package com.stergion.githubbackend.infrastructure.persistence.mongo.utils.types;

public class PullRequestReviewComment {
    public String login;
    public Github github;
    public String body;

    @Override
    public String toString() {
        return "{" +
                "login: '" + login + '\'' +
                ", github: " + github +
                ", body: '" + body + '\'' +
                "}";
    }
}
