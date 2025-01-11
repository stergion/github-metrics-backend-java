package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.common.batch.BatchProcessorConfig;
import com.stergion.githubbackend.domain.contirbutions.dto.CommitDTO;
import com.stergion.githubbackend.domain.contirbutions.mappers.CommitMapper;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Commit;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.CommitRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class CommitService extends ContributionService<CommitDTO, Commit> {
    @Inject
    CommitMapper commitMapper;

    @Inject
    public CommitService(CommitRepository commitRepository) {
        super(commitRepository);
    }


    @Override
    protected Commit mapDtoToEntity(CommitDTO dto, ObjectId userId, ObjectId repoId) {
        return commitMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected CommitDTO mapEntityToDto(Commit entity) {
        return commitMapper.toDTO(entity);
    }

    private Multi<CommitDTO> fetchCommits(String login, String owner, String name, LocalDate from,
                                          LocalDate to) {
        return client.getCommits(login, owner, name, from, to);
    }

    private Multi<List<CommitDTO>> fetchCommits(String login, String owner, String name,
                                                LocalDate from,
                                                LocalDate to, BatchProcessorConfig config) {
        return client.getCommitsBatched(login, owner, name, from, to, config);
    }

    public void fetchAndCreateCommits(String login, String owner, String name, LocalDate from,
                                      LocalDate to) {
        var config = BatchProcessorConfig.defaultConfig();

        var commits = fetchCommits(login, owner, name, from, to, config);
        createContributions(commits);
    }
}
