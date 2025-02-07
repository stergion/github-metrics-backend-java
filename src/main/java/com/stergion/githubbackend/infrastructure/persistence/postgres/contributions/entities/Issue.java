package com.stergion.githubbackend.infrastructure.persistence.postgres.contributions.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types.Label;
import com.stergion.githubbackend.infrastructure.persistence.utils.types.IssueState;
import io.quarkus.logging.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Issues")
public class Issue extends Contribution {

    @NotNull
    @PastOrPresent
    private LocalDateTime createdAt;

    @PastOrPresent
    private LocalDateTime closedAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private IssueState state;

    private String title;
    private String body;
    private int reactionsCount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Label> labels = new ArrayList<>();
    private String closer;

    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            Log.error(e.getClass() + ": " + e.getMessage() + ". \nCause: " + e.getCause());
            return "{id: %s, userLogin: %s, owner:%s, name:%s}".formatted(getId(),
                    getUser().getLogin(), getRepository().getOwner(), getRepository().getName());
        }
    }

    /*
     *********************
     * Getters / Setters *
     *********************
     */

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public void setReactionsCount(int reactionsCount) {
        this.reactionsCount = reactionsCount;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public String getCloser() {
        return closer;
    }

    public void setCloser(String closer) {
        this.closer = closer;
    }
}
