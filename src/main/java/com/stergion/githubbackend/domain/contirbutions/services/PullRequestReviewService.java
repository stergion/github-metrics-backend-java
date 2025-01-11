package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.PullRequestReviewDTO;
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
    public PullRequestReviewService(PullRequestReviewRepository pullRequestReviewRepository) {
        super(pullRequestReviewRepository);
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

    private Multi<PullRequestReviewDTO> fetchPullRequestReviews(String login, LocalDate from,
                                                                LocalDate to) {
        return client.getPullRequestReviews(login, from, to);
    }

    private Multi<List<PullRequestReviewDTO>> fetchPullRequestReviews(String login,
                                                                      LocalDate from, LocalDate to,
                                                                      BatchProcessorConfig config) {
        return client.getPullRequestReviewsBatched(login, from, to, config);
    }

    public void fetchAndPullRequestReviews(String login, LocalDate from, LocalDate to) {
        var config = BatchProcessorConfig.defaultConfig();
        var pullRequestReviews = fetchPullRequestReviews(login, from, to, config);
        createContributions(pullRequestReviews);
    }
}
