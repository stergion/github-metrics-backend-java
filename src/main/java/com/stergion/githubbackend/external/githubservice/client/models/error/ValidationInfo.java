package com.stergion.githubbackend.external.githubservice.client.models.error;

public record ValidationInfo(
        String value,
        String message,
        String location,
        String path
) {}
