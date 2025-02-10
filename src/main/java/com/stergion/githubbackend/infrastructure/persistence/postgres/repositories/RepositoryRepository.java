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
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RepositoryRepository implements PanacheRepositoryBase<RepositoryEntity, UUID> {
    @Inject
    EntityManagerFactory em;
    @Inject
    Mutiny.SessionFactory sessionFactory;


    public Uni<RepositoryEntity> findByGitHubId(String githubId) {
        return find("githubId", githubId).firstResult();
    }

    public Uni<List<RepositoryEntity>> findByOwner(String owner) {
        return find("owner", owner).list();
    }

    public Uni<RepositoryEntity> findByNameAndOwner(String owner, String name) {
        return find("owner = ?1 and name = ?2", owner, name).firstResult();
    }

    public Uni<List<RepositoryEntity>> findByNameAndOwners(List<NameWithOwner> repos) {

        if (repos.isEmpty()) {
            return Uni.createFrom().item(Collections.emptyList());
        }


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RepositoryEntity> query = cb.createQuery(RepositoryEntity.class);
        Root<RepositoryEntity> repositoryRoot = query.from(RepositoryEntity.class);

        Predicate[] predicates = repos.stream()
                                      .map(r -> cb.and(
                                              cb.equal(repositoryRoot.get("owner"), r.owner()),
                                              cb.equal(repositoryRoot.get("name"), r.name())
                                                      ))
                                      .toArray(Predicate[]::new);
        query.where(cb.or(predicates));

        return getSession().chain(i -> i.createQuery(query).getResultList());
    }

    @Override
    public Uni<Long> deleteAll() {
        String deleteRepositoryLabelsQuery = "delete from Repositories_Labels ";
        String deleteRepositoryLanguagesQuery = "delete from Repositories_Languages ";
        String deleteRepositoryTopicsQuery = "delete from Repositories_Topics ";
        String deleteRepositoriesQuery = "Delete from Repositories";
        String deleteLabelsQuery = "delete from Labels ";
        String deleteLanguagesQuery = "delete from Languages ";
        String deleteTopicsQuery = "delete from Topics ";

        return sessionFactory.withTransaction((session, tx) ->
                        session.createNativeQuery(deleteRepositoryLabelsQuery)
                               .executeUpdate()
                               .flatMap(result ->
                                               session.createNativeQuery(deleteRepositoryLanguagesQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteRepositoryTopicsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteRepositoriesQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteLabelsQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteLanguagesQuery)
                                                      .executeUpdate()
                                       )
                               .flatMap(result ->
                                               session.createNativeQuery(deleteTopicsQuery)
                                                      .executeUpdate()
                                       )
                               .map(Long::valueOf)
                                             );
    }

    public Uni<Long> deleteByOwner(String owner) {
        return delete("owner", owner);
    }
}
