package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.domain.users.User;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers.UserGHMapper;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.UserGH;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserClient {
    @RestClient
    GitHubServiceClient client;

    @Inject
    UserGHMapper mapper;

    public User getUserInfo(String login) {
        UserGH user = client.getUserInfo(login);
        return mapper.toDomain(user);
    }
}
