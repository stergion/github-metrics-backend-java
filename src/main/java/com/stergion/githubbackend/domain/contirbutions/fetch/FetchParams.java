package com.stergion.githubbackend.domain.contirbutions.fetch;

import com.stergion.githubbackend.domain.utils.types.NameWithOwner;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public record FetchParams(
        @NotNull
        String login,
        @NotNull
        LocalDateTime from,
        @NotNull
        LocalDateTime to,
        Optional<NameWithOwner> nameWithOwner
) {
    public FetchParams {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login cannot be null or blank");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range cannot be null");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("'to' date cannot be before 'from' date");
        }
    }

    public void validate() {
        // Base validation already done in constructor
    }

    public void validateForCommits() {
        validate();
        if (nameWithOwner.isEmpty()) {
            throw new IllegalArgumentException("Repository is required for commits");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String login;
        private LocalDateTime from;
        private LocalDateTime to;
        private Optional<NameWithOwner> nameWithOwner = Optional.empty();

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder dateRange(LocalDateTime from, LocalDateTime to) {
            this.from = from;
            this.to = to;
            return this;
        }

        public Builder repository(String owner, String name) {
            this.nameWithOwner = Optional.of(new NameWithOwner(owner, name));
            return this;
        }

        public FetchParams build() {
            return new FetchParams(login, from, to, nameWithOwner);
        }
    }
}
