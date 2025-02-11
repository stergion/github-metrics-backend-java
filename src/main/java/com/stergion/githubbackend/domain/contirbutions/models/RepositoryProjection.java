package com.stergion.githubbackend.domain.contirbutions.models;

import org.bson.types.ObjectId;

public record RepositoryProjection(ObjectId id, String owner, String name) {
}
