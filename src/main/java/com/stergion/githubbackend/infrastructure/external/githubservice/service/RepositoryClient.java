package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.RepositoryGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.utils.SseEventTransformer;
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

    public RepositoryGH getRepositoryInfo(String owner, String name) {
        return client.getRepository(owner, name);
    }

    public Multi<RepositoryGH> getRepositoriesCommittedTo(String login, LocalDate from,
                                                          LocalDate to) {
        return client.getRepositoriesCommittedTo(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, RepositoryGH.class));
    }

    public Multi<RepositoryGH> getRepositoriesContributedTo(String login, LocalDate from,
                                                            LocalDate to) {
        return client.getRepositoriesContributedTo(login, from, to)
                     .onItem()
                     .transform(event -> transformer.transform(event, RepositoryGH.class));
    }
}
