package com.stergion.githubbackend.infrastructure.persistence.contributions.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Filters;
import com.stergion.githubbackend.domain.contirbutions.search.ContributionSearchStrategy;
import com.stergion.githubbackend.domain.contirbutions.search.PagedResponse;
import com.stergion.githubbackend.domain.contirbutions.search.RangeValue;
import com.stergion.githubbackend.domain.contirbutions.search.criteria.BaseSearchCriteria;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.RangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.RangeableField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.TimeField;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.contributions.entities.Contribution;
import com.stergion.githubbackend.infrastructure.persistence.contributions.repositories.ContributionRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class MongoContributionSearchStrategy<T extends Contribution,
        U extends BaseSearchCriteria>
        implements ContributionSearchStrategy<T, U> {

    protected final ContributionRepository<T> repository;
    private final CodecRegistry codecRegistry;

    protected MongoContributionSearchStrategy(ContributionRepository<T> repository) {
        this.repository = repository;
        this.codecRegistry = repository != null ? repository.mongoCollection()
                                                            .getCodecRegistry() : null;
    }

    private static <T extends Contribution> PagedResponse<T> createEmptyResponse(
            BaseSearchCriteria criteria) {
        return PagedResponse.<T>builder()
                            .content(List.of())
                            .pageNumber(criteria.getPage())
                            .pageSize(criteria.getSize())
                            .totalElements(0)
                            .totalPages(0)
                            .build();
    }

    private static <F extends RangeableField<?>> BiConsumer<F, RangeValue<?>> getRangeValueBiConsumer(
            List<Bson> conditions) {
        Objects.requireNonNull(conditions, "conditions list must not be null");

        return (field, range) -> {
            Objects.requireNonNull(field, "field must not be null");
            Objects.requireNonNull(range, "range must not be null");

            List<Bson> rangeConditions = new ArrayList<>();

            range.getMin().ifPresent(min ->
                    rangeConditions.add(Filters.gte(field.getField(), min)));

            range.getMax().ifPresent(max ->
                    rangeConditions.add(Filters.lte(field.getField(), max)));

            if (!rangeConditions.isEmpty()) {
                conditions.add(Filters.and(rangeConditions));
            }
        };
    }

    @Override
    public Uni<PagedResponse<T>> search(U criteria) {
        List<Bson> pipeline = createAggregationPipeline(criteria);

        return repository.mongoCollection()
                         .aggregate(pipeline, Result.class)
                         .collect()
                         .asList()
                         .map(results -> createPagedResponse(results, criteria))
                         .onFailure()
                         .retry()
                         .withBackOff(Duration.ofMillis(500), Duration.ofSeconds(5))
                         .atMost(3);
    }

    public Document createSort(BaseSearchCriteria criteria) {
        return new Document(criteria.getSortBy().fieldName(),
                criteria.isAscending() ? 1 : -1);
    }

    private List<Bson> createAggregationPipeline(U criteria) {
        return List.of(
                Aggregates.match(createQuery(criteria)),
                Aggregates.facet(
                        new Facet("metadata",
                                Aggregates.count("total")),
                        new Facet("data",
                                Aggregates.sort(createSort(criteria)),
                                Aggregates.skip(criteria.getPage() * criteria.getSize()),
                                Aggregates.limit(criteria.getSize()))
                                )
                      );
    }

    abstract Bson createQuery(U criteria);

    protected List<Bson> createBaseConditions(U criteria) {
        List<Bson> conditions = new ArrayList<>();
        conditions.add(Filters.eq(CommonField.USER_LOGIN.fieldName(), criteria.getUserLogin()));
        conditions.addAll(createTimeFilters(criteria));
        conditions.addAll(createRangeFilters(criteria));
        return conditions;
    }

    protected List<Bson> createTimeFilters(U criteria) {
        List<Bson> conditions = new ArrayList<>();

        criteria.getTimeFilters().forEach(getRangeValueBiConsumer(conditions));

        return conditions;
    }

    protected List<Bson> createRangeFilters(U criteria) {
        List<Bson> conditions = new ArrayList<>();

        criteria.getRangeFilters().forEach(getRangeValueBiConsumer(conditions));

        return conditions;
    }

    private PagedResponse<T> createPagedResponse(List<Result> results, U criteria) {
        if (results.isEmpty()) {
            return createEmptyResponse(criteria);
        }

        Result result = results.getFirst();
        long totalElements = result.getMetadata().isEmpty() ? 0 :
                result.getMetadata().getFirst().getTotal();

        // Convert Documents back to objects
        List<T> data = result.getData().stream()
                             .map(this::decodeDocument)
                             .toList();

        return PagedResponse.<T>builder()
                            .content(data)
                            .pageNumber(criteria.getPage())
                            .pageSize(criteria.getSize())
                            .totalElements(totalElements)
                            .totalPages(
                                    (int) Math.ceil((double) totalElements / criteria.getSize()))
                            .build();
    }

    private T decodeDocument(Document doc) {
        try {
            return codecRegistry.get(repository.mongoCollection().getDocumentClass())
                                .decode(doc.toBsonDocument().asBsonReader(),
                                        DecoderContext.builder().build());
        } catch (Exception e) {
            Log.errorf("Failed to decode document: %s", e.getMessage());
            return null;
        }
    }


    public static class Result {
        private static final ObjectMapper MAPPER = JsonObjectMapper.create();
        private List<Metadata> metadata;
        private List<Document> data;

        public List<Metadata> getMetadata() {
            return metadata;
        }

        public void setMetadata(List<Metadata> metadata) {
            this.metadata = metadata;
        }

        public List<Document> getData() {
            return data;
        }

        public void setData(List<Document> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            try {
                return MAPPER.writeValueAsString(this);
            } catch (Exception ignored) {
                return super.toString();
            }
        }

        public static class Metadata {
            private long total;

            public long getTotal() {
                return total;
            }

            public void setTotal(long total) {
                this.total = total;
            }

            @Override
            public String toString() {
                try {
                    return MAPPER.writeValueAsString(this);
                }catch (Exception ignored) {
                    return super.toString();
                }
            }
        }
    }

}
