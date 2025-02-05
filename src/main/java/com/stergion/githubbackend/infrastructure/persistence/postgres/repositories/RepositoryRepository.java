package com.stergion.githubbackend.infrastructure.persistence.postgres.repositories;

import com.stergion.githubbackend.infrastructure.persistence.utils.types.NameWithOwner;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RepositoryRepository implements PanacheRepositoryBase<Repository, UUID> {
    @Inject
    EntityManagerFactory em;

    public Uni<Repository> findByGitHubId(String githubId) {
        return find("githubId", githubId).firstResult();
    }

    public Uni<List<Repository>> findByOwner(String owner) {
        return find("owner", owner).list();
    }

    public Uni<Repository> findByNameAndOwner(String owner, String name) {
        return find("owner = ?1 and name = ?2", owner, name).firstResult();
    }

    public Uni<List<Repository>> findByNameAndOwners(List<NameWithOwner> repos) {

        if (repos.isEmpty()) {
            return Uni.createFrom().item(Collections.emptyList());
        }


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Repository> query = cb.createQuery(Repository.class);
        Root<Repository> repositoryRoot = query.from(Repository.class);

        Predicate[] predicates = repos.stream()
                                      .map(r -> cb.and(
                                              cb.equal(repositoryRoot.get("owner"), r.owner()),
                                              cb.equal(repositoryRoot.get("name"), r.name())
                                                      ))
                                      .toArray(Predicate[]::new);
        query.where(cb.or(predicates));

        return getSession().chain(i -> i.createQuery(query).getResultList());
    }

    public Uni<Long> deleteByOwner(String owner) {
        return delete("owner", owner);
    }
}
