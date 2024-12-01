package com.stergion.githubbackend.core.externalservices.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.client.GitHubServiceClient;
import com.stergion.githubbackend.client.models.UserInfo;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserClient {
    @RestClient
    GitHubServiceClient client;

    static final ObjectMapper mapper = new ObjectMapper();

    public UserInfo getUserInfo(String login){
        return client.getUserInfo(login);
    }
}
