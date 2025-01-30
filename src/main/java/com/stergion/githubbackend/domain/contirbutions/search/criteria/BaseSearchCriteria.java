package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.RangeValue;
import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.RangeField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.SearchField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.TimeField;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class BaseSearchCriteria<R extends RangeField, T extends TimeField> {
    private final String userLogin;  // required
    private final int page;
    private final int size;
    private final SearchField sortBy;
    private final boolean ascending;
    private final Map<R, RangeValue<Integer>> rangeFilters;
    private final Map<T, RangeValue<LocalDateTime>> timeFilters;

    protected BaseSearchCriteria(BaseBuilder<?, R, T> builder) {
        this.userLogin = builder.userLogin;
        this.page = builder.page;
        this.size = builder.size;
        this.sortBy = builder.sortBy;
        this.ascending = builder.ascending;
        this.rangeFilters = Map.copyOf(builder.rangeFilters);
        this.timeFilters = Map.copyOf(builder.timeFilters);
        validate();
    }

    private void validate() {
        Objects.requireNonNull(userLogin, "userLogin must not be null");
        Objects.requireNonNull(sortBy, "sortBy must not be null");

        if (page < 0) {
            throw new IllegalArgumentException("page must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
    }

    public String getUserLogin() {
        return userLogin;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public SearchField getSortBy() {
        return sortBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public Map<R, RangeValue<Integer>> getRangeFilters() {
        return rangeFilters;
    }

    public Map<T, RangeValue<LocalDateTime>> getTimeFilters() {
        return timeFilters;
    }


    public abstract static class BaseBuilder<B extends BaseBuilder<B, R, T>, R extends RangeField
            , T extends TimeField> {
        private String userLogin;
        private int page = 0;  // default values
        private int size = 20;
        private SearchField sortBy = CommonField.CREATED_AT;  // default to common field
        private boolean ascending = false;
        private final Map<R, RangeValue<Integer>> rangeFilters = new HashMap<>();
        private final Map<T, RangeValue<LocalDateTime>> timeFilters = new HashMap<>();

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        public B userLogin(String userLogin) {
            this.userLogin = userLogin;
            return self();
        }

        public B page(int page) {
            this.page = page;
            return self();
        }

        public B size(int size) {
            this.size = size;
            return self();
        }

        public B sortBy(SearchField sortBy) {
            this.sortBy = sortBy;
            return self();
        }

        public B ascending(boolean ascending) {
            this.ascending = ascending;
            return self();
        }

        public B addRangeFilter(R field, RangeValue<Integer> range) {
            this.rangeFilters.put(field, range);
            return self();
        }

        public B addTimeFilter(T field, RangeValue<LocalDateTime> range) {
            this.timeFilters.put(field, range);
            return self();
        }

    }
}
