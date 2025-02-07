package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import com.stergion.githubbackend.infrastructure.persistence.postgres.repositories.Repository;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

@Entity
@Table(name = "Labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String label;

    private String description;

    @Override
    public String toString() {
        return "{id: %s, label: %s, description: %s}".formatted(id, label, description);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
