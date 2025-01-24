package com.stergion.githubbackend.domain.contirbutions.search.fields;

public sealed interface SearchField permits CommonField, CommitField, IssueField, PullRequestField,
        PullRequestReviewField, IssueCommentField {
    String fieldName();
}
