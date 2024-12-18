package com.stergion.githubbackend.external.githubservice.service;

import com.stergion.githubbackend.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.external.githubservice.client.models.success.UserInfo;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserClient {
    @RestClient
    GitHubServiceClient client;

    public UserInfo getUserInfo(String login) {
        return client.getUserInfo(login);
    }
}
