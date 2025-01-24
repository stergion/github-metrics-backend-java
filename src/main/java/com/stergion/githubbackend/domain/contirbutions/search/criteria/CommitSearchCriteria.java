package com.stergion.githubbackend.domain.contirbutions.search.criteria;

import java.time.LocalDateTime;
import java.util.Optional;

public class CommitSearchCriteria extends BaseSearchCriteria {
    private final String owner;
    private final String name;
    private final LocalDateTime since;
    private final LocalDateTime until;

    private CommitSearchCriteria(Builder builder) {
        super(builder);
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
        private String owner;
        private String name;
        private LocalDateTime since;
        private LocalDateTime until;

        public Builder owner(String name) {
            this.owner = name;
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

        public CommitSearchCriteria build() {
            return new CommitSearchCriteria(this);
        }
    }
}
