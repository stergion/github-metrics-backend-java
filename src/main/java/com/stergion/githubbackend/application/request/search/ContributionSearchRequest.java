package com.stergion.githubbackend.application.request.search;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.contirbutions.search.RangeValue;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitTimeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.RangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.TimeField;
import io.quarkus.arc.Arc;

import java.time.LocalDateTime;
import java.util.Map;

public sealed interface ContributionSearchRequest extends PaginatedSearchRequest
        permits CommitSearchRequest, IssueSearchRequest, IssueCommentSearchRequest,
        PullRequestSearchRequest, PullRequestReviewSearchRequest {
    String userLogin();

    String owner();

    String name();

    Map<? extends TimeField, RangeValue<LocalDateTime>> timeFilters();

    Map<? extends RangeField, RangeValue<Integer>> rangeFilters();


    @JsonAnySetter
    default void addFilter(String field, JsonNode jsonNode) {
        // Inject Quarkus default Object Mapper
        try (var instance = Arc.container().instance(ObjectMapper.class)) {
            var mapper = instance.get();

            try {
                var value = mapper.convertValue(jsonNode,
                        new TypeReference<RangeValue<Integer>>() {});
                addRangeFilter(field, value);
            } catch (Exception ignored) {
            }

            try {
                var value = mapper.convertValue(jsonNode,
                        new TypeReference<RangeValue<LocalDateTime>>() {});
                addTimeFilter(field, value);
            } catch (Exception ignored) {
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    ;

    void addTimeFilter(String field, RangeValue<LocalDateTime> value);

    void addRangeFilter(String field, RangeValue<Integer> value);

}
