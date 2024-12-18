package com.stergion.githubbackend.external.githubservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.external.githubservice.client.models.success.UserInfo;
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
