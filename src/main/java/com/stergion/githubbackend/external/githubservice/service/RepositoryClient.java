package com.stergion.githubbackend.external.githubservice.service;

import com.stergion.githubbackend.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.external.githubservice.client.models.success.Repository;
import com.stergion.githubbackend.external.githubservice.utils.SseEventTransformer;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

@ApplicationScoped
public class RepositoryClient {
    @RestClient
    GitHubServiceClient client;

    @Inject
    SseEventTransformer transformer;

    public Repository getRepositoryInfo(String owner, String name) {
        return client.getRepository(owner, name);
    }

    public Multi<Repository> getRepositoriesCommittedTo(String login, LocalDate from,
                                                        LocalDate to) {
        return client.getRepositoriesCommittedTo(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, Repository.class));
    }

    public Multi<Repository> getRepositoriesContributedTo(String login, LocalDate from,
                                                          LocalDate to) {
        return client.getRepositoriesContributedTo(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, Repository.class));
    }
}
