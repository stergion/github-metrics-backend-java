package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.dto.CommitDTO;
import com.stergion.githubbackend.domain.contirbutions.fetch.CommitFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.mappers.CommitMapper;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.entities.Commit;
import com.stergion.githubbackend.infrastructure.persistence.contirbutions.repositories.CommitRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CommitService extends ContributionService<CommitDTO, Commit> {
    @Inject
    CommitMapper commitMapper;

    @Inject
    public CommitService(CommitRepository commitRepository, CommitFetchStrategy fetchStrategy) {
        super(commitRepository, fetchStrategy);
    }


    @Override
    protected Commit mapDtoToEntity(CommitDTO dto, ObjectId userId, ObjectId repoId) {
        return commitMapper.toEntity(dto, userId, repoId);
    }

    @Override
    protected CommitDTO mapEntityToDto(Commit entity) {
        return commitMapper.toDTO(entity);
    }

    public Multi<List<CommitDTO>> fetchAndCreateCommits(String login,
                                                        String owner, String name,
                                                        LocalDateTime from, LocalDateTime to) {

        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .repository(owner, name)
                                .build();

        return fetchAndCreate(params);
    }
}
