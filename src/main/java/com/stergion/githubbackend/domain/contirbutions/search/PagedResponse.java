package com.stergion.githubbackend.domain.contirbutions.search;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PagedResponse<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    private PagedResponse(Builder<T> builder) {
        this.content = Objects.requireNonNull(builder.content);
        this.pageNumber = builder.pageNumber;
        this.pageSize = builder.pageSize;
        this.totalElements = builder.totalElements;
        this.totalPages = builder.totalPages;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T, R> PagedResponse<R> map(PagedResponse<T> response, Function<T, R> mapper) {
        return PagedResponse.<R>builder()
                            .content(response.getContent().stream().map(mapper).toList())
                            .pageNumber(response.getPageNumber())
                            .pageSize(response.getPageSize())
                            .totalElements(response.getTotalElements())
                            .totalPages(response.getTotalPages())
                            .build();
    }

    public List<T> getContent() {
        return content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isFirst() {
        return pageNumber == 0;
    }

    public boolean isLast() {
        return pageNumber == totalPages - 1;
    }

    public boolean hasNext() {
        return pageNumber < totalPages - 1;
    }

    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    public static class Builder<T> {
        private List<T> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;

        public Builder<T> content(List<T> content) {
            this.content = content;
            return this;
        }

        public Builder<T> pageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder<T> pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder<T> totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public PagedResponse<T> build() {
            return new PagedResponse<>(this);
        }
    }
}
