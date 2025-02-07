package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Override
    public String toString() {
        return "{id: %s, name: %s}".formatted(id, name);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
