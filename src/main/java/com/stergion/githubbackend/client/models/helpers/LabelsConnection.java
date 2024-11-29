package com.stergion.githubbackend.client.models.helpers;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

/**
 * Represents the labels connection with total count and nodes
 */
public record LabelsConnection(
        @PositiveOrZero(message = "Total count must be non-negative")
        int totalCount,

        @NotNull(message = "Label nodes cannot be null")
        List<LabelNode> nodes
) {
}