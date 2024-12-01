package com.stergion.githubbackend.external.githubservice.client.models.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.net.URI;

/**
 * Represents a file changed in a commit with its associated metadata
 */
public record CommitFile(
        @NotBlank(message = "Filename cannot be blank")
        String filename,

        @PositiveOrZero(message = "Number of additions must be non-negative")
        int additions,

        @PositiveOrZero(message = "Number of deletions must be non-negative")
        int deletions,

        @PositiveOrZero(message = "Number of changes must be non-negative")
        int changes,

        String status,

        URI rawUrl,

        URI blobUrl,

        String patch,

        String previousFilename,

        @NotBlank(message = "SHA cannot be blank")
        String sha,

        URI contentsUrl
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
            return String.format("CommitFile[filename=%s, sha=%s]", filename, sha);
        }
    }
}