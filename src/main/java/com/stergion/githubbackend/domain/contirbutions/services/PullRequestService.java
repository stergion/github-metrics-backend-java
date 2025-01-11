package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestDTO;
import com.stergion.githubbackend.domain.contirbutions.mappers.PullRequestMapper;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.PullRequest;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.PullRequestRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PullRequestService extends ContributionService<PullRequestDTO, PullRequest> {
    @Inject
    PullRequestMapper pullRequestMapper;

    @Inject
    public PullRequestService(PullRequestRepository pullRequestRepository) {
        super(pullRequestRepository);
    }

    @Override
    protected PullRequest mapDtoToEntity(PullRequestDTO dto, ObjectId userId, ObjectId repoId) {
        return pullRequestMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected PullRequestDTO mapEntityToDto(PullRequest entity) {
        return pullRequestMapper.toDTO(entity);
    }

    private Multi<PullRequestDTO> fetchPullRequests(String login, LocalDate from, LocalDate to) {
        return client.getPullRequests(login, from, to);
    }

    private Multi<List<PullRequestDTO>> fetchPullRequests(String login,
                                                          LocalDate from, LocalDate to,
                                                          BatchProcessorConfig config) {
        return client.getPullRequestsBatched(login, from, to, config);
    }

    public void fetchAndCreatePullRequests(String login, LocalDate from, LocalDate to) {
        var config = BatchProcessorConfig.defaultConfig();
        var pullRequests = fetchPullRequests(login, from, to, config);
        createContributions(pullRequests);
    }
}
