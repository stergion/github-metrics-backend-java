package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class CommitComment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String author;
    private LocalDateTime publishedAt;
    private int position;
    private int reactionsCount;
    private String body;

    public CommitComment() {
    }

    public CommitComment(String author, LocalDateTime publishedAt, int position, int reactionsCount,
                         String body) {
        this.author = author;
        this.publishedAt = publishedAt;
        this.position = position;
        this.reactionsCount = reactionsCount;
        this.body = body;
    }

    @Override
    public String toString() {
        return ("{id: %s, author: %s, publishedAt: %s, position: %s, reactionsCount: %s, body: " +
                "%s}").formatted(id, author, publishedAt, position, reactionsCount, body);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public void setReactionsCount(int reactionsCount) {
        this.reactionsCount = reactionsCount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
