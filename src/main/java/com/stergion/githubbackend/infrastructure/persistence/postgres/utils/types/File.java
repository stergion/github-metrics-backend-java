package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fileName;
    private String baseName;
    private String extension;
    private String path;
    private String status;
    private int additions;
    private int deletions;
    private int changes;
    private String patch;

    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            return "{fileName: %s, additions: %d, deletions: %d}".formatted(fileName, additions,
                    deletions);
        }
    }
}
