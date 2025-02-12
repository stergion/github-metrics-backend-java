package com.stergion.githubbackend.domain.contirbutions.services;

import com.stergion.githubbackend.domain.contirbutions.models.Commit;
import com.stergion.githubbackend.domain.contirbutions.fetch.CommitFetchStrategy;
import com.stergion.githubbackend.domain.contirbutions.fetch.FetchParams;
import com.stergion.githubbackend.domain.contirbutions.mappers.CommitMapper;
import com.stergion.githubbackend.domain.contirbutions.search.CommitSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.CommitSearchCriteria;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.entities.CommitEntity;
import com.stergion.githubbackend.infrastructure.persistence.mongo.contributions.repositories.MongoCommitRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CommitService extends ContributionService<Commit, CommitEntity> {
    @Inject
    CommitMapper commitMapper;
    @Inject
    CommitSearchStrategy searchStrategy;

    @Inject
    public CommitService(MongoCommitRepository commitRepository, CommitFetchStrategy fetchStrategy) {
        super(commitRepository, fetchStrategy);
    }


    @Override
    protected CommitEntity mapDomainToEntity(Commit commit) {
        return commitMapper.toEntity(commit);
    }

    @Override
    protected Commit mapEntityToDomain(CommitEntity entity) {
        return commitMapper.toDomain(entity);
    }

    public Multi<List<Commit>> fetchAndCreateCommits(String login,
                                                     String owner, String name,
                                                     LocalDateTime from, LocalDateTime to) {

        var params = FetchParams.builder()
                                .login(login)
                                .dateRange(from, to)
                                .repository(owner, name)
                                .build();

        return fetchAndCreate(params);
    }

    public Uni<PagedResponse<Commit>> search(CommitSearchCriteria criteria) {
        return searchStrategy.search(criteria)
                             .map(response -> PagedResponse.map(response, commitMapper::toDomain));
    }
}
