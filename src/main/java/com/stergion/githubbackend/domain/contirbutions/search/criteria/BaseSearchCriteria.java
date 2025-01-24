package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.contirbutions.search.fields.CommonField;
import com.stergion.githubbackend.domain.contirbutions.search.fields.SearchField;

import java.util.Objects;

public abstract class BaseSearchCriteria {
    private final String userLogin;  // required
    private final int page;
    private final int size;
    private final SearchField sortBy;
    private final boolean ascending;

    protected BaseSearchCriteria(BaseBuilder<?> builder) {
        this.userLogin = Objects.requireNonNull(builder.userLogin, "userLogin must not be null");
        this.page = builder.page;
        this.size = builder.size;
        this.sortBy = builder.sortBy;
        this.ascending = builder.ascending;
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


    public abstract static class BaseBuilder<T extends BaseBuilder<T>> {
        private String userLogin;
        private int page = 0;  // default values
        private int size = 20;
        private SearchField sortBy = CommonField.CREATED_AT;  // default to common field
        private boolean ascending = false;

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        public T userLogin(String userLogin) {
            this.userLogin = userLogin;
            return self();
        }

        public T page(int page) {
            this.page = page;
            return self();
        }

        public T size(int size) {
            this.size = size;
            return self();
        }

        public T sortBy(SearchField sortBy) {
            this.sortBy = sortBy;
            return self();
        }

        public T ascending(boolean ascending) {
            this.ascending = ascending;
            return self();
        }
    }
}
