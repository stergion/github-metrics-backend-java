package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestDTO;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.PullRequestFetchStrategy;
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
    public PullRequestService(PullRequestRepository pullRequestRepository,
                              PullRequestFetchStrategy fetchStrategy) {
        super(pullRequestRepository, fetchStrategy);
    }

    @Override
    protected PullRequest mapDtoToEntity(PullRequestDTO dto, ObjectId userId, ObjectId repoId) {
        return pullRequestMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected PullRequestDTO mapEntityToDto(PullRequest entity) {
        return pullRequestMapper.toDTO(entity);
    }

    public Multi<List<PullRequestDTO>> fetchAndCreatePullRequests(String login, LocalDate from,
                                                                  LocalDate to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();

        return fetchAndCreate(params);
    }
}
