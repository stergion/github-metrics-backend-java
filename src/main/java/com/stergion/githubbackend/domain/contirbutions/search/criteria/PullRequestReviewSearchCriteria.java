package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import com.stergion.githubbackend.domain.utils.types.PullRequestReviewState;

import java.time.LocalDateTime;
import java.util.Optional;

public class PullRequestReviewSearchCriteria extends BaseSearchCriteria {
    private final PullRequestReviewState state;
    private final String owner;
    private final String name;
    private final LocalDateTime since;
    private final LocalDateTime until;

    private PullRequestReviewSearchCriteria(Builder builder) {
        super(builder);
        this.state = builder.state;
        this.owner = builder.owner;
        this.name = builder.name;
        this.since = builder.since;
        this.until = builder.until;
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate() {
        if (until != null && since != null && until.isBefore(since)) {
            throw new IllegalArgumentException("until must be after since");
        }
    }

    public Optional<PullRequestReviewState> getState() {
        return Optional.ofNullable(state);
    }

    public Optional<String> getOwner() {
        return Optional.ofNullable(owner);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<LocalDateTime> getSince() {
        return Optional.ofNullable(since);
    }

    public Optional<LocalDateTime> getUntil() {
        return Optional.ofNullable(until);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private PullRequestReviewState state;
        private String owner;
        private String name;
        private LocalDateTime since;
        private LocalDateTime until;

        public Builder state(PullRequestReviewState state) {
            this.state = state;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder since(LocalDateTime since) {
            this.since = since;
            return this;
        }

        public Builder until(LocalDateTime until) {
            this.until = until;
            return this;
        }

        public PullRequestReviewSearchCriteria build() {
            return new PullRequestReviewSearchCriteria(this);
        }
    }
}
