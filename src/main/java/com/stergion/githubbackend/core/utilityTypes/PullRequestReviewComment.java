package com.stergion.githubbackend.core.utilityTypes;

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
