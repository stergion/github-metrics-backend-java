package com.stergion.githubbackend.core.externalservices.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.client.GitHubServiceClient;
import com.stergion.githubbackend.client.models.Repository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

@ApplicationScoped
public class RepositoryClient {
    @RestClient
    GitHubServiceClient client;

    static final ObjectMapper mapper = new ObjectMapper();

    public Repository getRepositoryInfo(String owner, String name) {
        return client.getRepository(owner, name);
    }

    public Multi<Repository> getRepositoriesCommittedTo(String login, LocalDate from,
                                                        LocalDate to) {
        return client.getRepositoriesCommittedTo(login, from, to)
                     .onItem()
                     .transform(repository -> mapper.convertValue(repository, Repository.class));
    }

    public Multi<Repository> getRepositoriesContributedTo(String login, LocalDate from,
                                                          LocalDate to) {
        return client.getRepositoriesContributedTo(login, from, to)
                     .onItem()
                     .transform(repository -> mapper.convertValue(repository, Repository.class));
    }
}
