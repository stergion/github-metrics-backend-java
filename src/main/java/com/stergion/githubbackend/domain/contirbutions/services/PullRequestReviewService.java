package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestReviewDTO;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.fetch.PullRequestReviewFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.mappers.PullRequestReviewMapper;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.PullRequestReview;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.PullRequestReviewRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PullRequestReviewService
        extends ContributionService<PullRequestReviewDTO, PullRequestReview> {
    @Inject
    PullRequestReviewMapper pullRequestReviewMapper;

    @Inject
    public PullRequestReviewService(PullRequestReviewRepository pullRequestReviewRepository,
                                    PullRequestReviewFetchStrategy fetchStrategy) {
        super(pullRequestReviewRepository, fetchStrategy);
    }

    @Override
    protected PullRequestReview mapDtoToEntity(PullRequestReviewDTO dto, ObjectId userId,
                                               ObjectId repoId) {
        return pullRequestReviewMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected PullRequestReviewDTO mapEntityToDto(PullRequestReview entity) {
        return pullRequestReviewMapper.toDTO(entity);
    }

    public Multi<List<PullRequestReviewDTO>> fetchAndCreatePullRequestReviews(String login,
                                                                              LocalDate from,
                                                                              LocalDate to) {
        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .build();
        return fetchAndCreate(params);
    }
}
