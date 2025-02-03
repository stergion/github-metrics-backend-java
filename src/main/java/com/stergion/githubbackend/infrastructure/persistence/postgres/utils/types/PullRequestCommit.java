package com.stergion.githubbackend.infrastructure.persistence.postgres.utils.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stergion.githubbackend.domain.utils.JsonObjectMapper;
import com.stergion.githubbackend.infrastructure.persistence.mongo.utilityTypes.Github;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.net.URI;
import java.util.UUID;

@Entity
public class PullRequestCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String githubId;

    private URI githubUrl;

    private int additions;

    private int deletions;

    private int changedFiles;


    private static final ObjectMapper MAPPER = JsonObjectMapper.create();

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (Exception e) {
            return "{id: %s, githubId: %s }".formatted(id, githubId);
        }
    }
}
