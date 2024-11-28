package com.stergion.githubbackend.client.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URI;

/**
 * Represents a GitHub user's public information.
 * All URIs are validated at construction time.
 */
public record UserInfo(
        @NotBlank(message = "User ID cannot be blank")
        String id,

        @NotBlank(message = "Login cannot be blank")
        String login,

        // Optional fields can be null
        String name,

        String bio,

        @NotNull(message = "Profile URL cannot be null")
        URI url,

        @NotNull(message = "Email cannot be null")
        @Email(message = "Must be a valid email address")
        String email,

        @NotNull(message = "Avatar URL cannot be null")
        URI avatarUrl,

        String twitterUsername,

        URI websiteUrl
) {
    private static final ObjectWriter WRITER = new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .withoutAttribute("jacksonObjectMapper");

    /**
     * Compact constructor for validation
     */
    public UserInfo {
        // Additional custom validation could be added here if needed
    }

    /**
     * Returns a user's display name, falling back to 'login' if 'name' is not set
     *
     * @return the display name to use
     */
    public String getDisplayName() {
        return (name != null && !name.isBlank()) ? name : login;
    }

    @Override
    public String toString() {
        try {
            return WRITER.writeValueAsString(this);
        } catch (Exception e) {
            return String.format("UserInfo[id=%s, login=%s]", id, login);
        }
    }
}