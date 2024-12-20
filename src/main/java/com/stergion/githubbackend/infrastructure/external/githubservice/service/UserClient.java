package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.UserGH;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserClient {
    @RestClient
    GitHubServiceClient client;

    public UserGH getUserInfo(String login) {
        return client.getUserInfo(login);
    }
}
