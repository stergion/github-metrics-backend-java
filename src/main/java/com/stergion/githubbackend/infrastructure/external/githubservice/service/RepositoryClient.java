package com.stergion.githubbackend.infrastructure.external.githubservice.service;

import com.stergion.githubbackend.common.batch.BatchProcessor;
import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.repositories.Repository;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.GitHubServiceClient;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.mappers.RepositoryGHMapper;
import com.stergion.githubbackend.infrastructure.external.githubservice.client.models.success.RepositoryGH;
import com.stergion.githubbackend.infrastructure.external.githubservice.utils.SseEventTransformer;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RepositoryClient {
    @RestClient
    GitHubServiceClient client;

    @Inject
    SseEventTransformer transformer;

    @Inject
    RepositoryGHMapper mapper;

    public Repository getRepositoryInfo(String owner, String name) {
        RepositoryGH repo = client.getRepository(owner, name);
        return mapper.toDomain(repo);
    }

    public Multi<Repository> getRepositoriesCommittedTo(String login, LocalDateTime from,
                                                        LocalDateTime to) {
        return client.getRepositoriesCommittedTo(login, from, to)
                     .map(event -> transformer.transform(event, RepositoryGH.class))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(mapper::toDomain);

    }

    public Multi<List<Repository>> getRepositoriesCommittedToBatched(String login,
                                                                     LocalDateTime from,
                                                                     LocalDateTime to,
                                                                     BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getRepositoriesCommittedTo(login, from, to)
                           .map(event -> transformer.transform(event, RepositoryGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(mapper::toDomain)
                             .toMulti();

    }

    public Multi<Repository> getRepositoriesContributedTo(String login, LocalDateTime from,
                                                          LocalDateTime to) {
        return client.getRepositoriesContributedTo(login, from, to)
                     .map(event -> transformer.transform(event, RepositoryGH.class))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(mapper::toDomain);
    }

    public Multi<List<Repository>> getRepositoriesContributedToBatched(String login,
                                                                       LocalDateTime from,
                                                                       LocalDateTime to,
                                                                       BatchProcessorConfig config) {
        BatchProcessor batchProcessor = new BatchProcessor(config);

        var stream = client.getRepositoriesContributedTo(login, from, to)
                           .map(event -> transformer.transform(event, RepositoryGH.class));

        return batchProcessor.processBatch(stream)
                             .transform(mapper::toDomain)
                             .toMulti();

    }
}
