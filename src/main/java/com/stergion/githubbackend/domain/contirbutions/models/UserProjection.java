package com.stergion.githubbackend.domain.contirbutions.models;

import org.bson.types.ObjectId;

public record UserProjection(ObjectId id, String login, String name) {}
