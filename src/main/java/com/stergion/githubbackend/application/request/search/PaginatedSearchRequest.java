package com.stergion.githubbackend.application.request.search;

import com.stergion.githubbackend.domain.contirbutions.search.fields.SearchField;

public interface PaginatedSearchRequest {
    int page();

    int size();

    SearchField sortBy();

    boolean ascending();
}
