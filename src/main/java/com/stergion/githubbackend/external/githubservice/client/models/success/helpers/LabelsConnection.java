package com.stergion.githubbackend.external.githubservice.client.models.success.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        private static final ObjectWriter WRITER = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writerWithDefaultPrettyPrinter()
                .withoutAttribute("jacksonObjectMapper");

        @Override
        public String toString() {
                try {
                        return WRITER.writeValueAsString(this);
                } catch (Exception e) {
                        return String.format("LabelsConnection[totalCount=%s, nodes=%s]", totalCount(), nodes().toString());
                }
        }
}