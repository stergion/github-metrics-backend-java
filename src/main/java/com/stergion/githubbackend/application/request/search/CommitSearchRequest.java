package com.stergion.githubbackend.application.request.search;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.contirbutions.search.RangeValue;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitRangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommitTimeField;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import io.quarkus.logging.Log;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class CommitSearchRequest implements ContributionSearchRequest {
    private static final ObjectMapper MAPPER = JsonObjectMapper.create();
    // time
    public final Map<CommitTimeField, RangeValue<LocalDateTime>> timeFilters =
            new HashMap<>();
    // range
    public final Map<CommitRangeField, RangeValue<Integer>> rangeFilters =
            new HashMap<>();
    @Min(0)
    public int page;
    @Min(20)
    public int size;
    public CommitField sortBy = CommitField.COMMITED_DATE;
    public boolean ascending = true;
    @NotBlank
    @Size(min = 1, max = 128)
    public String userLogin;
    public String owner;
    String name;

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            Log.warn(e.getMessage());
            return "{user: %s}".formatted(userLogin);
        }
    }

    @JsonSetter("sortBy")
    public void setSortBy(String sortBy) {
        for (var e : CommitField.values()) {
            if (e.toString().equalsIgnoreCase(sortBy)
                    || e.fieldName().equalsIgnoreCase(sortBy)) {
                this.sortBy = e;
            }

        }
    }

    public void addTimeFilter(String field, RangeValue<LocalDateTime> value) {
        for (var e : CommitTimeField.values()) {
            if (e.toString().equalsIgnoreCase(field)
                    || e.getField().equalsIgnoreCase(field)) {
                timeFilters.put(e, value);
            }

        }
    }

    public void addRangeFilter(String field, RangeValue<Integer> value) {
        for (var e : CommitRangeField.values()) {
            if (e.toString().equalsIgnoreCase(field)
                    || e.getField().equalsIgnoreCase(field)) {
                rangeFilters.put(e, value);
            }

        }
    }

    @Override
    public int page() {
        return this.page;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public CommitField sortBy() {
        return this.sortBy;
    }

    @Override
    public boolean ascending() {
        return this.ascending;
    }

    @Override
    public String userLogin() {
        return this.userLogin;
    }

    @Override
    public String owner() {
        return this.owner;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Map<CommitTimeField, RangeValue<LocalDateTime>> timeFilters() {
        return this.timeFilters;
    }

    @Override
    public Map<CommitRangeField, RangeValue<Integer>> rangeFilters() {
        return this.rangeFilters;
    }
}
